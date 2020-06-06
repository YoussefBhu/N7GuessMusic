var mysql = require('mysql');
var pool  = mysql.createPool({
    host: 'localhost',
  user: 'root',
  password: '',
  database: 'musicapp',
});
var getConnection = function (cb) {
    pool.getConnection(function (err, connection) {
        //if(err) throw err;
        //pass the error to the cb instead of throwing it
        if(err) {
          return cb(err);
        }
        cb(null, connection);
    });
};
module.exports = getConnection;