const app = require('express')();
const http = require('http').Server(app);
const io = require('socket.io')(http);
const mysql = require('mysql');
const uuid1 = require('uuid/v1');
const uuid2 = require('uuid/v4');
const sqlConnection = mysql.createConnection({
  host: 'localhost',
  user: 'root',
  password: 'dldydgus8014',
  database: 'abroad'
});
const TAG = '---DEBUG TAG---   :  ';

sqlConnection.connect();
http.listen(80, () => {
  console.log('80 포트에 연결 됨.');
});

var chatMembers = {};

io.on('connection', (socket) => {
  console.log(TAG + 'user connected on NO NAMESPACE');
  socket.emit('checkSignedUser');
  socket.on('userData', (userInfo) => {
    sqlConnection.query('SELECT * FROM member WHERE userI=?', userInfo.userId, (err, rows, fields) => {
      if (err) {
        console.log(err);
        socket.emit('sqlError', err);
      } else {
        socket.emit('signedUser');
      }
    })
  })
})

var signIn = io.of('/singIn');
signIn.on('connection', (socket) => {
  console.log(TAG + 'user connected on SIGN IN NAMESPACE');
  socket.on('signInRequest', (signInData) => {
    console.log(TAG + '**SIGN IN ** Client request SIGN IN');
    sqlConnection.query('SELECT * FROM member WHERE userId=?', signInData.userId, (err, rows, fields) => {
      if (err) {
        console.log('****************** SQL ERROR ******************');
        console.log(err);
        console.log('***********************************************');
      } else {
        if (rows[0] == undefined) {
          socket.emit('signInFailed');
          console.log(TAG + '**SIGN IN ** SIGN IN Failed');
        } else {
          if (rows[0].userId == signInData.userId && rows[0].password == signInData.password) {
            socket.emit('signInSuccess');
            console.log(TAG + '**SIGN IN ** SIGN IN SUCCESS');
          }
        }
      }
    }) // signInQuery End
  }) //signInRequest EMIT End


}) //NAMESPACE
