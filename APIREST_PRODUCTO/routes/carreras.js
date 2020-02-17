var express = require('express');
var router = express.Router();
var carrera_controller = require('../controllers/carrerasfinalizadas');
router.get('/test', carrera_controller.test);
router.post('/create', carrera_controller.carrera_create);
router.get('/list', carrera_controller.carreras_list);

module.exports = router;