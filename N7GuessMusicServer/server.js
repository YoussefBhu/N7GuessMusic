const express = require('express')
const app = express()
var mysql = require('mysql')
const bodyParser = require('body-parser')
var existe =  require('./functions/existe')
var makeid =  require('./functions/makeid')
var MakePlaylist = require('./functions/MakePlaylist')
var PartyOwner = require('./functions/PartyOwner')
var getName = require('./functions/getName')
var getConnection = require('./functions/db');
const port = 3000
app.use(bodyParser.json());


app.get('/', (req, res) => res.send('Hello World!'))
/
app.post('/Registre', (req, res)=>{
  
  username =  req.body.username 
  password = req.body.password 
  name = req.body.name 
  var sql ="select * from comptes where username='"+username+"' "
  getConnection(function(err, connection){
  connection.query(sql, function (err, result) {
    if (err) throw err;
    if(result.length == 0 ){
        var sql1 ="INSERT INTO comptes VALUES (NULL,'"+username+"','"+password+"','"+name+"');"
      connection.query(sql1, function (err, result) {
        if (err) throw err;
        var responce = {
          success : 1 ,
          Message : "vous avez creer votre compte avec succées"
        }
       
        res.json(responce)
      })
    }
    else {
      var responce = {
        success : 0 , 
        Message : "ce nom de compte deja existe essayer avec un autre"
      }
      res.json(responce)
    }
    
})
connection.release();
  })
})

app.post('/Connexion', (req, res)=>{

    username =  req.body.username 
    password = req.body.password 
    console.log(username)
    console.log(password)
    getConnection(function(err, connection){
    sql = "select * from comptes where username='"+username+"' and password='"+password+"' "
    connection.query(sql, function (err, result) {
        if(result.length == 0 ){
            var responce =  {
               success : 0 , 
               Message : "compte n'existe pas ou mot de passe incorrect"
            }
            res.json(responce)
        }
        else{
          var responce =  {
              success : 1 , 
              name : result[0].Name , 
              userid : result[0].id
          }
          res.json(responce)
        }
        connection.release();
    })
  })
})

app.post('/Create', (req, res)=>{
  getConnection(function(err, connection){
          (async function(){
          
            var userid =  req.body.userid
            var genre = req.body.genre
            var code = makeid(6)
            var test = await existe(code)
           // console.log(test)
            while(test){
               // console.log('loop')
                code = makeid(6)
                test = await existe(code)
            }
            var playlist = await MakePlaylist(genre)
            console.log("code = "+code+"  genere = "+genre+" userid ="+userid+" JSON.stringify(playlist) = "+JSON.stringify(playlist))
            var sql =  "INSERT INTO parties VALUES ('"+code+"','"+genre+"',"+userid+",'"+JSON.stringify(playlist)+"',1,1)"
           
            connection.query(sql, function (err, result) {
              if (err) res.json({success : 0 , Message : "Try again"});
              else{ 
                (async function(){
              var responce = {
                success : 1,
                PartyCode : code, 
                PartyOwner : await PartyOwner(code),
                playlist : playlist 
              }
              res.json(responce)
              connection.release();
              res.end
            }) ()
              }
              
             
        })
    
          }) ()
          
        
    })
  })
/

app.post('/Join', (req, res)=>{
    var userid =  req.body.userid 
    var code = req.body.code
    var sql = "SELECT * from parties WHERE code='"+code+"'"
    getConnection(function(err, connection){
    connection.query(sql, function (err, result) {
      if(result.length == 0){
        var responce = {
            success : 0 , 
            Message : "code invalide"
        }
        res.json(responce)
      }
      else if(result[0].stats == 0){
        var responce ={
            success : 0 , 
            Message :"cette partie est finie"
        }
        res.json(responce)
      }
      else {
        (async function(){
        var sql2 = "UPDATE parties SET nplayers="+(result[0].nplayers + 1)+" where code ='"+result[0].code+"'"
        connection.query(sql2, function (err, result1) {})
        var responce = {
          success : 1 , 
          PartyCode : result[0].code, 
          PartyOwner : await PartyOwner(code),
          playlist : JSON.parse(result[0].data)
        }
        res.json(responce)
      })()
      }

    })
    connection.release();
  })
})

app.post('/Score',(req,res)=>{
  var userid = req.body.userid
  var code = req.body.code 
  var score = req.body.score 
  
  getConnection(function(err, connection){
  sqll =  "SELECT * from scores where id_party = '"+code+"' and id_compte = "+userid+""
  connection.query(sqll, function (err, scores) {
    if(scores.length == 0){ 
  sql = "INSERT into scores VALUES ("+null+", '"+code+"' , "+userid+","+score+","+null+" )"
  connection.query(sql, function (err, result) {
    if (err) {res.json({success : -1})}
    var responce = {
      success : 1 , 
    }
    res.json(responce)
  })
  }
  else {
    var responce = {
      success : 0 , 
      Message : "vous avez deja un score dans cette partie"
    }
    res.json(responce)
  }
})
connection.release();
  })
})


app.post('/GetScores',(req,res)=>{
  var code = req.body.code 
  sql = "SELECT * from scores where id_party ='"+code+"'"
  getConnection(function(err, connection){
  connection.query(sql, function (err, result) {
    (async function(){
    var responce = []
      for (var i= 0 ; i <result.length ; i++){
         var x ={
           name : await getName(connection , result[i].id_compte),
           score : result[i].score
         }
         responce.push(x)
      }
      res.json({scores : responce})
    })()
  })
})
    })


/*
var sql ="select * from json where id=1 "

connection.query(sql, function (err, result) {
    if (err) throw err;
    jsonOB = JSON.parse(result[0].data)
    console.log(jsonOB.name)
    
})
*/

app.listen(port)