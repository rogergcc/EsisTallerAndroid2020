var mongoose = require('mongoose');
var Schema = mongoose.Schema;

var CarrerasFinalizadasSchema = new Schema({
	socket_id_cliente: {
		type: String, required: true
	},
	
	socket_id_conductor:{
		type: String, required: true
	},
	nombre_conductor: {
		type: String, required: true
	},
	fecha_registro: {
        type: String,
    },
    hora_finalizacion: {
        type: String,
        
    }
});
// Export the model
module.exports = mongoose.model('CarrerasFinalizadas', CarrerasFinalizadasSchema);
