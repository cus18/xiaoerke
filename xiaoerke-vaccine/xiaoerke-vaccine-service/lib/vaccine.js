var header = require('./../util/header');

module.exports = {
    demo: function(req, res) {
        header.set(req, res);
        return res.send({
            status: '请求成功'
        });
    }
};