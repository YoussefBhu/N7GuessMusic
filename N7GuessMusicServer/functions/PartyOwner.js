var mysql = require('mysql');
var getConnection = require('./db');
module.exports = function (code){
    return new Promise(( resolve, reject ) => {
        getConnection(function(err, connection){
        var sql = "SELECT * FROM parties where code='"+code+"'"
                connection.query(sql, function (err,result) {

                    if(result.length == 0 ) 
                    {
                     resolve(null)
                    }
                    else 
                    {
                     var sql1 = "SELECT * FROM comptes where id="+result[0].creator
                     connection.query(sql1, function (err,player) {
                       resolve(player[0].username+"("+player[0].Name+")")
                     })
                    }
                  
                })
            connection.release()
            })
    })

}