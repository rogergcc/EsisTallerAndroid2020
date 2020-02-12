var express = require('express');var app = express();
var bodyParser = require('body-parser');
var product = require('./routes/product');

var mongoose = require('mongoose');
var eConfig = JSON.parse(process.env.APP_CONFIG);
var dev_db_url = "mongodb://" + eConfig.mongo.user + ":" + "1234qwer" + "@" +
eConfig.mongo.hostString;

var mongoDB = process.env.MONGODB_URI || dev_db_url;

mongoose.connect(mongoDB,{ useUnifiedTopology: true,useNewUrlParser: true });
mongoose.Promise = global.Promise;var db = mongoose.connection;

db.on('error', console.error.bind(console, 'MongoDB connection error:'));
app.use(bodyParser.json());
app.use(bodyParser.urlencoded({extended: false}));
app.use('/products', product);
var port = process.env.PORT || 5000;
app.listen(port, () => {
console.log('Server is up and running on port numner ' + port);
});