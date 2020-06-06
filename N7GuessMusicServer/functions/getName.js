var mysql = require('mysql');
module.exports = function (connection ,id){
    return new Promise(( resolve, reject ) => {
        var sql = "SELECT * FROM comptes where id="+id
        connection.query(sql, function (err,player) {
          resolve(player[0].username+"("+player[0].Name+")")
        })
    })
}