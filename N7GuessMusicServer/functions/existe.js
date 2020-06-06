var mysql = require('mysql');
var getConnection = require('./db');
module.exports = function (code){
    return new Promise( ( resolve, reject ) => {
        getConnection(function(err, connection){
        var sql = "SELECT * FROM parties where code='"+code+"'"
                connection.query(sql, function (err,result) {

                    if(result.length == 0 ) 
                    {
                    resolve(false)
                    }
                    else 
                    resolve(true)
                  
                })
                connection.release()
        })
        
    })
}
