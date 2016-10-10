var header = require('./../util/header'),
    http= require("http");

module.exports = {
    getVaccineStation: function(req, res) {
        header.set(req, res);
        /*return res.send({
            status: '请求成功'
        });*/

        var data = {};

        data = JSON.stringify(data);
        console.log(data);
        var opt = {
            method: "POST",
            host: "120.25.161.33",
            port: 80,
            path: "/titan/vaccineUser/getVaccineStation",
            headers: {
                "Content-Type": 'application/json',
                "Content-Length": data.length
            }
        };

        var req = http.request(opt, function (serverFeedback) {
            if (serverFeedback.statusCode == 200) {
                var body = "";
                serverFeedback.on('data', function (data) { body += data; })
                    .on('end', function () { res.send(200, body); });
            }
            else {
                res.send(500, "error");
            }
        });
        req.write(data + "\n");
        req.end();
    },
    saveBabyVaccine: function(req, res) {
        header.set(req, res);
        /*return res.send({
         status: '请求成功'
         });*/
        console.log('发来的数据:', req.body)
        var data = req.body;

        data = JSON.stringify(data);
        var opt = {
            method: "POST",
            host: "120.25.161.33",
            port: 80,
            path: "/titan/vaccineUser/saveBabyVaccine",
            headers: {
                "Content-Type": 'application/json',
                "Content-Length": data.length
            }
        };

        var req = http.request(opt, function (serverFeedback) {
            if (serverFeedback.statusCode == 200) {
                var body = "";
                serverFeedback.on('data', function (data) { body += data; })
                    .on('end', function () { res.send(200, body); });
            }
            else {
                res.send(500, "error");
            }
        });
        req.write(data + "\n");
        req.end();
    }
};