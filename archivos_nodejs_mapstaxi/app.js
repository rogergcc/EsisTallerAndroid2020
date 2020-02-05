const express = require('express');const app = express();
const http = require('http').Server(app);const io = require('socket.io')(http);
var port = process.env.PORT || 5000;var numuser =0;
io.on('connection', (socket)=>{
	numuser=numuser+1;
	console.log("Se conecto iduser: "+ socket.id + " numuser: " + numuser);
	socket.on('pedirtaxi',pedirtaxi);
	function pedirtaxi (datos,cb){
		datos.socket=socket.id;
		io.emit('solicitudtaxi',datos);
		cb('OK');
	}
	socket.on('disconnect', function(){
		numuser=numuser-1;
		console.log("Se desconecto iduser: "+ socket.id+ " numuser: " + numuser);
	});

	socket.on('accept',accept);
	function accept (datos,cb){
		console.log(datos);
		console.log("taxiencontrado");

		//io to es para emitir solo al un pasajero especifico, solo al socketid que emitio accept
		//este socket id del usuario
		io.to(datos.id).emit('taxiencontrado',datos);
		cb('OK');
	}
	socket.on('taxilocation',taxilocation);
	function taxilocation (datos,cb){
		console.log(datos);
		
		//recuperar el socketid de la bd
		io.to(datos.id).emit('localizacion',datos);
		cb('OK');
	}
	socket.on('abordo',abordo);
	function abordo (datos,cb){
		console.log(datos);
		console.log("fin proceso");
		io.to(datos.id).emit('Abordo',datos);
		cb('OK');
	}

	// TArea EMITER AL CONDUCTOR Y SOOCKET.ON EN CLIENTE
	//PARA  CUANDO 2 CLIENTE QUIEREN SOLICITAR UN MISMO CONDUCTOR, se debe validar con la BD quien solicituo 1ero y luego el 2do

});
http.listen(port, ()=>{
	console.log('connected to port: ' + port)
});