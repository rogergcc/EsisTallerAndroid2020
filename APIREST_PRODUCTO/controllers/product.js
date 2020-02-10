var Product = require('../models/product');
exports.test = function (req, res) {
	res.send('Saludos del test de server');
};
exports.product_create = function (req, res) {
	var product = new Product(
	{
		name: req.body.name,
		price: req.body.price
	}
	);
	product.save(function (err,producto) {
		if (err) {
			return next(err);
		}
		console.log(producto);
		console.log(producto._id.toString);
		res.send('Productos creados correctamente');
	})
};
exports.product_list = function (req, res) {
	Product.find({}, function (err, products) {
		if (err) return next(err);
		console.log(products)
		res.send(products);
	})
};

exports.product_details = function (req, res) {
	reqname=req.params.name;
	Product.findOne({name:reqname}, function (err, product) {
		if (err) return next(err);
		res.send(product);
	})
};
exports.product_update = function (req, res) {
	Product.findByIdAndUpdate(req.params.id, {$set: req.body},
		function (err, product) {
			if (err) return next(err);
			res.send('Producto actualizado.');
		});
};
exports.product_delete = function (req, res) {
	Product.findByIdAndRemove(req.params.id, function (err) {
		if (err) return next(err);
		res.send('Eliminacion exitosa');
	})
};