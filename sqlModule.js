const mysql = require('mysql');
const sqlConnection = mysql.createConnection({
  host: 'localhost',
  user: 'root',
  password: 'dldydgus8014',
  database: 'abroad'
});
sqlConnection.connect();

var queryResult = new Object();
exports.sqlQuery = function(sql) {
  sqlConnection.query(sql, (err, rows, fileds) => {
    if(err) {
      console.log('****************** SQL ERROR ******************');
      queryResult.err = true;
      queryResult.value = err;
    } else {
      queryResult.err = false;
      queryResult.value = rows;
    }
  })
  return queryResult;
}

exports.paramsSqlQuery = function(sql, params) {
   sqlConnection.query(sql, params, (err, rows, fields) => {
     if(err) {
       console.log('****************** SQL ERROR ******************');
       queryResult.err = true;
       queryResult.value = err;
       return queryResult;
     } else {
       queryResult.err = false;
       queryResult.value = rows;
       return queryResult;
     }
   })
   return queryResult;
}
