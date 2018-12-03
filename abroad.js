const express = require('express');
const app = express();
const http = require('http').Server(app);
const io = require('socket.io')(http);
const mysql = require('mysql');
const async = require('async')
const fs = require('fs');
const uuid1 = require('uuid/v1');
const uuid2 = require('uuid/v4');
const sqlConnection = mysql.createConnection({
  host: 'localhost',
  user: 'root',
  password: 'dldydgus8014',
  database: 'abroad'
});
sqlConnection.connect();
const TAG = '---DEBUG TAG---   :  ';

app.use(express.static('profile'));
app.get('/profile', function(req, res) {
  console.log('소켓아닌거 접속');
  var string = req.query.id;
  res.sendFile(__dirname + '/profile/' + string);
});

http.listen(80, () => {
  console.log('80 포트에 연결 됨.');
});

var members = {};
io.set('heartbeat timeout', 60000);
io.set('heartbeat interval', 25000);
io.on('connection', (socket) => {
  socket.on('reconnect', () => {
    console.log('리컨넥중');
  })
  console.log('*!*! NO NAMESPACE SOCKET.ID >>' + socket.id + '  *!*!');
  socket.on('checkSignedUser', (userInfo) => {
    console.log(TAG + 'no name checkSignedUser client emit');
    sqlConnection.query('SELECT userName, e_mail, password, userUuid FROM member', (err, rows, fields) => {
      if (err) {
        console.log(err);
        socket.emit('sqlError', {
          result: err
        });
      } else {
        for (var i in rows) {
          if (rows[i].e_mail == userInfo.e_mail) {
            if (rows[i].password == userInfo.password && rows[i].userUuid == userInfo.userUuid) {
              socket.emit('signedUser', {
                result: 'ok'
              });
              console.log('ok - no name signed User');
            }
          }
        }
      }
    })
  })
  socket.on('disconnect', (reason) => {
    console.log(TAG + '--DISCONNECT--  reason : ' + reason);
  })
})

var signIn = io.of('/signIn');
signIn.on('connection', (socket) => {
  console.log('*!*  SIGN IN SOCKET.ID >>' + socket.id + '  *!*!');
  socket.on('checkSignedUser', (userInfo) => {
    console.log(TAG + 'sing in checkSignedUser client emit');
    sqlConnection.query('SELECT userName, password, userUuid FROM member', (err, rows, fields) => {
      if (err) {
        console.log(err);
        socket.emit('sqlError', {
          result: err
        });
      } else {
        for (var i in rows) {
          if (rows[i].e_mail == userInfo.e_mail) {
            if (rows[i].password == userInfo.password && rows[i].userUuid == userInfo.userUuid) {
              socket.emit('signedUser', {
                result: 'ok'
              });
              console.log('ok - signed User');
            }
          }
        }
      }
    })
  })
  socket.on('signInRequest', (signInData) => {
    console.log(TAG + '**SIGN IN ** Client request SIGN IN');
    sqlConnection.query('SELECT userName, e_mail, password, userUuid FROM member WHERE e_mail=?', signInData.e_mail, (err, rows, fields) => {
      if (err) {
        console.log(err);
      } else {
        if (rows[0] == undefined) {
          socket.emit('signInFailed', {
            result: 'no log user signed'
          });
          console.log(TAG + '**SIGN IN ** SIGN IN Failed');
        } else {
          if (rows[0].e_mail == signInData.e_mail && rows[0].password == signInData.password) {
            socket.emit('signInSuccess', rows);
            console.log(TAG + '**SIGN IN ** SIGN IN SUCCESS');
          } else {
            console.log(TAG + '!!SIGN IN ** FAILED');
            socket.emit('signInFailed', {
              result: 'ok'
            });
          }
        }
      }
    }) // signInQuery End
  }) //signInRequest EMIT End
}) //SIGNIN NAMESPACE END

var signUp = io.of('/signUp');
signUp.on('connection', (socket) => {
  console.log('!*!*  SIGN UP SOCKET.ID >>' + socket.id + '  !*!*');
  socket.on('signUpRequest', (signUpData) => {
    console.log('signUpRequest');
    async.waterfall([
      (callback) => {
        sqlConnection.query('commit', (err, rows, fields) => {
          if (err) {
            callback('commitError');
          } else {
            callback(null, 'commitSuccess');
          }
        })
      },
      (arg, callback) => {
        sqlConnection.query('SELECT * FROM member WHERE e_mail=?', signUpData.e_mail, (err, rows, fields) => {
          if (err) {
            callback('sqlError');
            console.log(err);
            console.log('sql에러 2???');
          } else {
            var userUuid = uuid1();
            console.log('헬로1');
            if (rows[0] == undefined) {
              console.log('헬로2');
              if (signUpData.hasOwnProperty('profile')) {
                fs.writeFile('profile/' + userUuid + '.jpeg', signUpData.profile, 'binary', (err) => {
                  if (err) {
                    callback('make profile image failed');
                    console.log(err);
                  } else {
                    callback(null, 'success', userUuid);
                  }
                })
              } else {
                callback(null, 'success', userUuid);
              }
            } else {
              callback('already exist user');
              console.log('exist user');
            }
          }
        })
      },
      (arg, userUuid, callback) => {
        var sqlInsertMemberParams = new Array();
        sqlInsertMemberParams.push(signUpData.userName);
        sqlInsertMemberParams.push(signUpData.password);
        sqlInsertMemberParams.push(signUpData.e_mail);
        sqlInsertMemberParams.push(socket.id);
        sqlInsertMemberParams.push(userUuid);
        sqlInsertMemberParams.push(signUpData.gender);
        sqlInsertMemberParams.push(signUpData.DOB);
        console.log('**************');
        console.log(sqlInsertMemberParams);
        if (arg == 'success') {
          sqlConnection.query('INSERT INTO member(userName, password, e_mail, socketId, userUuid, gender, DOB) VALUES(?, ?, ?, ? ,?, ?, ?)', sqlInsertMemberParams, (err, rows, fileds) => {
            if (err) {
              console.log(err);
              callback('sqlError');
            } else {
              callback(null, {
                result: 'ok'
              })
            }
          })
        }
      }
    ], (err, result) => {
      console.log(err);
      if (err) {
        if (err == 'sqlError') {
          console.log(err);
          sqlConnection.query('rollback', (err, rows, fields) => {
            socket.emit('sqlError');
          });
        } else if (err == 'already exist user') {
          socket.emit('signUpFailed', {
            result: 'already exist user'
          });
          console.log('already exist user');
        }
      } else {
        console.log('회원가입 성공');
        socket.emit('signUpSuccess', {
          result: 'ok'
        });
      }
    })
  })
}) //SIGN UP NAMESPACE END

var chat = io.of('/chat');
chat.on('connection', (socket) => {
  socket.on('reconnect', () => {
    console.log('리컨넥중');
  })
  console.log(socket.adapter.rooms);
  console.log('!*!*  CHAT SOCKET.ID >>' + socket.id + '  !*!*');
  socket.on('chatConnected', (userInfo) => {
    console.log('헤이1');
    if (userInfo != null && userInfo != undefined) {
      console.log('헤이2');
      try {
        members[userInfo.userUuid] = socket;
        console.log('헤이3' + userInfo.userUuid);
        sqlConnection.query('SELECT room FROM member WHERE userUuid =? ', userInfo.userUuid, (err, rows, fields) => {
          if (err) {
            console.log(err);
            socket.emit('chatConnectedFailed');
          } else {
            if (rows[0].room != null) {
              var userRoom = JSON.parse(rows[0].room);
              socket.join(userRoom);
            }
            socket.emit('chatConnectedSuccess')
          }
        })
      } catch (exception) {
        socket.emit('chatConnectedFailed', {
          result: 'check userId'
        });
      }
    } else {
      socket.emit('chatConnectedFailed', {
        result: 'check userId'
      });
    }
  }) //chatConnected event Handle
  socket.on('makeChatRoom', (makeRoomData) => {
    var makeRoomFlag = true;
    var asyncParallelList = new Array();
    sqlConnection.query('commit');
    var stringifyIds = new Array();
    for (let i = 0; i < makeRoomData.length; i++) {
      if (members[makeRoomData[i]] == undefined || members[makeRoomData[i]] == null) {
        console.log('유저 접속 아님');
        socket.emit('makeRoomFailed');
        makeRoomFlag = false;
        return;
      }
      stringifyIds.push(JSON.stringify(makeRoomData[i]))
    }
    if (makeRoomFlag) {
      var roomName = uuid2();
      for (var i = 0; i < makeRoomData.length; i++) {
        ((i) => {
          asyncParallelList.push((callback) => {
            sqlConnection.query('SELECT room FROM member WHERE userUuid = ?', makeRoomData[i], (err, rows, fields) => {
              if (err) {
                console.log(err);
                callback('sqlError');
              } else {
                var updateParams = new Array();
                if (rows[0].room == null) {
                  var userRoom = new Array();
                  userRoom.push(roomName);
                  updateParams.push(JSON.stringify(userRoom));
                  updateParams.push(makeRoomData[i]);
                } else {
                  var userRoom = JSON.parse(rows[0].room);
                  userRoom.push(roomName);
                  updateParams.push(JSON.stringify(userRoom));
                  updateParams.push(makeRoomData[i]);
                }
                console.log(updateParams);
                sqlConnection.query('UPDATE member SET room = ? WHERE userUuid = ?', updateParams, (err, rows, fields) => {
                  if (err) {
                    console.log(err);
                    callback('sqlError');
                  } else {
                    callback(null, 'success');
                  }
                })
              }
            })
          })
        })(i);
      }
      asyncParallelList.push((callback) => {
        console.log(stringifyIds.join(','));
        var uuids = new Array();
        sqlConnection.query('SELECT userUuid FROM member WHERE userUuid IN (' + stringifyIds.join(',') + ')', (err, rows, fields) => {
          if (err) {
            console.log(err);
            callback('sqlError');
          } else {
            for (let i = 0; i < rows.length; i++) {
              uuids.push(rows[i].userUuid);
            }
            sqlConnection.query('INSERT INTO chatList VALUES(?, ?, ?)', [roomName, JSON.stringify(uuids), JSON.stringify(makeRoomData)], (err, rows, fields) => {
              if (err) {
                console.log(err);
                callback('sqlError')
              } else {
                callback(null, 'success');
              }
            })
          }
        })
      })
      async.parallel(asyncParallelList, (err, result) => {
        if (err) {
          console.log(err);
          console.log('룸만들기 실패');
          sqlConnection.query('rollback', (err, rows, fields) => {
            socket.emit('sqlError');
          })
        } else {
          console.log('룸만들기 성공');
          for (let i = 0; i < makeRoomData.length; i++) {
            if (i == 0) {
              members[makeRoomData[i]].join(roomName);
              socket.emit('makeRoomSuccess', {
                roomName: roomName,
                joinMembers: makeRoomData
              });
            } else {
              members[makeRoomData[i]].join(roomName);
              members[makeRoomData[i]].emit('newRoomChat', {
                roomName: roomName,
                joinMembers: makeRoomData
              });
            }
          }
        }
      })
    }
  }) //EVENT MAKE CHAT ROOM END
  socket.on('chatList', (chatListData) => {
    if (chatListData != undefined && chatListData != null && chatListData.userUuid != null && chatListData.userUuid != undefined) {
      var chatListParams = '%' + chatListData.userUuid + '%';
      sqlConnection.query('SELECT * FROM chatList WHERE memberUuid LIKE ?', chatListParams, (err, rows, params) => {
        if (err) {
          console.log(err);
          socket.emit('sqlError');
        } else {
          if (rows[0] != undefined) {
            var chatListArray = new Array();
            var asyncParallelFunct = new Array();
            for (var i = 0; i < rows.length; i++) {
              ((i) => {
                asyncParallelFunct.push((callback) => {
                  var result = {
                    roomName: rows[i].roomName,
                    // joinMembers: rows[i].joinMembers,
                    joinMembers: JSON.parse(rows[i].joinMembers),
                    lastMessage: undefined
                  };
                  var lastMessageQuery = new Array();
                  var roomName = rows[i].roomName;
                  lastMessageQuery.push(roomName);
                  lastMessageQuery.push(roomName);
                  sqlConnection.query('SELECT message FROM chatMessage WHERE room = ? AND timestamp = (SELECT MAX(timestamp) FROM chatMessage WHERE room =?)', lastMessageQuery, (err, rows, fields) => {
                    if (err) {
                      console.log(err);
                      socket.emit('sqlError');
                      callback(err);
                    } else {
                      if (rows[0] != undefined) {
                        result.lastMessage = rows[0].message;
                      }
                      chatListArray.push(result);
                      callback(null, result);
                    }
                  })
                })
              })(i);
            }
            async.parallel(asyncParallelFunct, (err, result) => {
              if (err) {
                console.log(err);
                socket.emit('chatListFailed');
              } else {
                console.log(result);
                socket.emit('chatListSuccess', chatListArray);
              }
            })
          }
        }
      })
    } else {
      socket.emit('chatListFailed', {
        result: 'userUuid is null'
      });
    }
  })
  socket.on('chatMessage', (chatMessageData) => {
    sqlConnection.query('SELECT sendMessageId, timestamp, message FROM chatMessage WHERE room = ? ORDER BY timestamp', chatMessageData.roomName, (err, rows, fields) => {
      if (err) {
        socket.emit('sqlError');
        console.log(err);
      } else {
        console.log('여기여기' + rows[0]);
        socket.emit('chatMessageSuccess', rows);
      }
    })
  })
  socket.on('sendMessage', (messageData) => {
    sqlConnection.query('INSERT INTO chatMessage(sendMessageId, room, message) VALUES(?, ?, ?)', [messageData.userId, messageData.roomName, messageData.message], (err, rows, fields) => {
      if (err) {
        console.log(err);
        socket.emit('sendMessageFailed');
      } else {
        if (members[messageData.userId] === socket) {
          console.log('true');
        } else {
          console.log('false');
        }
        console.log(messageData.roomName);
        io.of('/chat').to(messageData.roomName).adapter.clients((err, clients) => {
          console.log('접속된 소켓들!         ' + clients);
        })
        socket.emit('sendMessageSuccess');
        chat.to(messageData.roomName).emit('receiveMessage', {
          sendMessageId: messageData.userId,
          roomName: messageData.roomName,
          message: messageData.message
        });
        chat.to(messageData.roomName).adapter.clientRooms(socket.id, (err, rooms) => {
          console.log('소켓의 룸들!            ' + rooms);
        })
      }
    })
  })
}) //CHAT NAMESPACE END

var map = io.of('/chat/map');
map.on('connection', (socket) => {
  console.log('!*!*  MAP SOCKET.ID >>' + socket.id + '  !*!*');
  socket.on('getUserLocation', (locationData) => {
    console.log('&&&&&&&&&&&&&&&&&&&&&');
    console.log('jobService');
    async.waterfall([
      (callback) => {
        sqlConnection.query('commit', (err, rows, fields) => {
          if (err) callback('commitError');
          else callback(null, 'commitSuccess');
        });
      },
      (arg, callback) => {
        sqlConnection.query('SELECT * FROM userLocation WHERE userUuid=?', locationData.userUuid, (err, rows, fields) => {
          if (err) {
            console.log('에러1');
            callback('sqlError');
          } else {
            var sqlParams = new Array();
            if (rows[0] == undefined) {
              sqlParams.push(locationData.userName);
              sqlParams.push(locationData.userUuid);
              sqlParams.push(locationData.latitude);
              sqlParams.push(locationData.longitude);
              callback(null, 'not exist userLocation', sqlParams);
            } else {
              sqlParams.push(locationData.latitude);
              sqlParams.push(locationData.longitude);
              sqlParams.push(locationData.userUuid);
              callback(null, 'exist userLocation', sqlParams);
            }
          }
        })
      },
      (flag, params, callback) => {
        if (flag == 'not exist userLocation') {
          sqlConnection.query('INSERT INTO userLocation(userName, userUuid, latitude, longitude) VALUES(?, ?, ?, ?)', params, (err, rows, fields) => {
            if (err) {
              console.log('에러2');
              console.log(err);
              callback('sqlError');
            } else {
              callback(null, 'success');
            }
          })
        } else {
          sqlConnection.query('UPDATE userLocation SET latitude=?, longitude=? WHERE userUuid=?', params, (err, rows, fields) => {
            if (err) {
              console.log('에러3');
              callback('sqlError');
            } else {
              callback(null, 'success');
            }
          })
        }
      },
      (status, callback) => {
        var selectUserLocationParams = new Array();
        selectUserLocationParams.push(locationData.latitude);
        selectUserLocationParams.push(locationData.longitude);
        selectUserLocationParams.push(locationData.latitude);
        selectUserLocationParams.push(locationData.userUuid);
        sqlConnection.query('select userName, userUuid, distance, latitude, longitude from member Natural join (SELECT *,(6371*acos(cos(radians(?))*cos(radians(latitude))*cos(radians(longitude)-radians(?))+sin(radians(?))*sin(radians(latitude))))AS distance FROM userLocation HAVING distance <= 7 ORDER BY distance LIMIT 0,5000) AS T WHERE userUuid <> ?', selectUserLocationParams, (err, rows, fields) => {
          if (err) {
            console.log(err);
            console.log('에러4');
            callback('rollback');
          } else {
            if (rows != undefined) {
              socket.binary(true).emit('getUserLocationSuccess', rows);
              console.log(TAG + 'near user query finished and emit');
            } else {
              socket.emit('noNearUser', {
                result: null
              });
              console.log(TAG + 'near user query finished and emit but null');
            }
          }
        })
      }
    ], (err, result) => {
      if (err == 'sqlError') {
        socket.emit('sqlError');
      } else if (err == 'rollback') {
        sqlConnection.query('rollback', (err, rows, fields) => {});
      }
    })
  })



  socket.on('saveLocation', (locationData) => {
    console.log(TAG + 'user emit saveLocation' + locationData);
    async.waterfall([
      (callback) => {
        sqlConnection.query('commit', (err, rows, fields) => {
          if (err) callback('commitError');
          else callback(null, 'commitSuccess');
        });
      },
      (arg, callback) => {
        sqlConnection.query('SELECT * FROM userLocation WHERE userUuid=?', locationData.userUuid, (err, rows, fields) => {
          if (err) {
            console.log('에러1');
            callback('sqlError');
          } else {
            var sqlParams = new Array();
            if (rows[0] == undefined) {
              sqlParams.push(locationData.userName);
              sqlParams.push(locationData.userUuid);
              sqlParams.push(locationData.latitude);
              sqlParams.push(locationData.longitude);
              callback(null, 'not exist userLocation', sqlParams);
            } else {
              sqlParams.push(locationData.latitude);
              sqlParams.push(locationData.longitude);
              sqlParams.push(locationData.userUuid);
              callback(null, 'exist userLocation', sqlParams);
            }
          }
        })
      },
      (flag, params, callback) => {
        if (flag == 'not exist userLocation') {
          sqlConnection.query('INSERT INTO userLocation(userName, userUuid, latitude, longitude) VALUES(?, ?, ?, ?)', params, (err, rows, fields) => {
            if (err) {
              console.log('에러2');
              console.log(err);
              callback('sqlError');
            } else {
              callback(null, 'success');
            }
          })
        } else {
          sqlConnection.query('UPDATE userLocation SET latitude=?, longitude=? WHERE userUuid=?', params, (err, rows, fields) => {
            if (err) {
              console.log('에러3');
              callback('sqlError');
            } else {
              callback(null, 'success');
            }
          })
        }
      },
      (status, callback) => {
        var selectUserLocationParams = new Array();
        selectUserLocationParams.push(locationData.latitude);
        selectUserLocationParams.push(locationData.longitude);
        selectUserLocationParams.push(locationData.latitude);
        selectUserLocationParams.push(locationData.distance);
        selectUserLocationParams.push(locationData.userUuid);
        sqlConnection.query('select userName, userUuid, distance, latitude, longitude from member Natural join (SELECT *,(6371*acos(cos(radians(?))*cos(radians(latitude))*cos(radians(longitude)-radians(?))+sin(radians(?))*sin(radians(latitude))))AS distance FROM userLocation HAVING distance <= ? ORDER BY distance LIMIT 0,5000) AS T WHERE userUuid <> ?', selectUserLocationParams, (err, rows, fields) => {
          if (err) {
            console.log(err);
            console.log('에러4');
            callback('rollback');
          } else {
            if (rows != undefined) {
              socket.binary(true).emit('saveLocationSuccess', rows);
              console.log(TAG + 'near user query finished and emit');
            } else {
              socket.emit('noNearUser', {
                result: null
              });
              console.log(TAG + 'near user query finished and emit but null');
            }
          }
        })
      }
    ], (err, result) => {
      if (err == 'sqlError') {
        socket.emit('sqlError');
      } else if (err == 'rollback') {
        sqlConnection.query('rollback', (err, rows, fields) => {});
      }
    })
  }) //EVENT SAVE_LOCATION End
})
