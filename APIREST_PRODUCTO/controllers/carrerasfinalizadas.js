var Carreras = require('../models/carrerasfinalizadas');
exports.test = function (req, res) {
	res.send('Saludos del test de server');
};
exports.carrera_create = function (req, res) {
	var carrera = new Carreras(
	{
		// socket_id_cliente: req.body.name,
		// socket_id_conductor: req.body.name		
		socket_id_cliente: req.body.socket_id_cliente,
		socket_id_conductor: req.body.socket_id_conductor,
		nombre_conductor: req.body.nombre_conductor,
		fecha_registro: req.body.fecha_registro,
		hora_finalizacion: req.body.hora_finalizacion
	}
	);
	carrera.save(function (err,carrera) {
		if (err) {
			return next(err);
		}
		console.log(carrera);
		console.log(carrera._id.toString);
		res.send('Carrera Finalizada');
	})
};
exports.carreras_list = function (req, res) {
	Carreras.find({}, function (err, carreras) {
		if (err) return next(err);
		console.log(carreras)
		res.send(carreras);
	})
};
