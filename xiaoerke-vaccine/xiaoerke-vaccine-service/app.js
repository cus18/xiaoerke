var express = require('express'),
    http = require('http'),
    path = require('path'),
    log4js = require('log4js'),
    router = require('./lib/router'),
    config = require('./config.json'),
    app = express();


app.set('port', process.env.PORT || 8088);

app.use(express.favicon('./favicon.ico'));
app.use(express.logger('dev'));
app.use(express.json());
app.use(express.urlencoded());
app.use(express.methodOverride());
app.use(express.bodyParser({ uploadDir: './upload' }));
app.use(require('connect-multiparty')());

//日志配置
log4js.configure({
    appenders: [{
        type: 'console'
    }, {
        type: 'file',
        filename:__dirname +  '/logs/access.log',
        maxLogSize: 1024,
        backups: 3,
        category: 'normal'
    }],
    replaceConsole: true
});

var logger = log4js.getLogger('normal');
logger.setLevel('INFO');
app.use(log4js.connectLogger(logger, {
    level: log4js.levels.INFO
}));


app.use(app.router);
app.use(express.static(path.join(__dirname, 'pc-web')));
app.use(express.static(path.join(__dirname, 'upload')));


if ('development' === app.get('env')) {
    app.use(express.errorHandler());
}
//路由
router(app);



http.createServer(app).listen(app.get('port'), function() {
    console.log('API服务启动' + app.get('port'));
});
