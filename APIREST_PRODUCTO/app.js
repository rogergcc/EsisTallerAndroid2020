var isproduction = true;
if (process.env.NODE_ENV !== 'production') {
    require('dotenv').config()
    isproduction=false;
}

var express = require('express');var app = express();
var bodyParser = require('body-parser');

var product = require('./routes/product');
var carreras = require('./routes/carreras');

var mongoose = require('mongoose');



var mongoDB;
if(isproduction){
    var eConfig = JSON.parse(process.env.APP_CONFIG);
    var dev_db_url = "mongodb://" + eConfig.mongo.user + ":" + "1234qwer" + "@" +
    eConfig.mongo.hostString;

    mongoDB = process.env.MONGODB_URI || dev_db_url;
}else{
    mongoDB = process.env.MONGODB_URI ;
}


mongoose.connect(mongoDB,{ useUnifiedTopology: true,useNewUrlParser: true })
        .then(connection => {
            this.connection = connection;
            console.log('Conexion a Base de Datos Exitosa');
        }).catch(error => console.log(error));

mongoose.Promise = global.Promise;var db = mongoose.connection;

db.on('error', console.error.bind(console, 'MongoDB connection error:'));
app.use(bodyParser.json());
app.use(bodyParser.urlencoded({extended: false}));
app.use('/products', product);

app.use('/carreras', carreras);

var port = process.env.PORT || 5000;
app.listen(port, () => {
console.log('Server is up and running on port numner ' + port);
});