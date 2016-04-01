angular.module('directives', [])
    .directive('autofocus', function () {
        return function (scope, element) {
            element.focus();
        }
    })
    .directive('calendar', function ($state, Util, AgendaAddItem, AgendaUpdateItem) {
        return function (scope, element, attrs) {
            var array = [];
            setTimeout(function () {
                element.fullCalendar({
                    header: {
                        left: 'title',
                        center: '',
                        right: 'today prev,next'
                    },
                    firstDay: 0,
                    businessHours: {
                        start: '00:00',
                        end: '23:59'
                    },
                    eventLimit: true,
                    selectable: true,
                    select: function (start, end) {

                        var title = prompt('');
                        if (title) {
                            element.fullCalendar('renderEvent', {
                                title: title,
                                start: start,
                                end: end
                            }, true);
                            AgendaAddItem.get({
                                doctorId: scope.userId,
                                tenantId: 1,
                                date: Number(start.valueOf()),
                                time: Number(start.valueOf()),
                                title: title
                            });
                        }
                    },
                    eventClick: function (calEvent) {
                        var title = prompt('', calEvent.title);
                        if (title) {
                            calEvent.title = title;
                            element.fullCalendar('updateEvent', calEvent);
                            AgendaUpdateItem.get({
                                itemId: calEvent._id,
                                date: Number(calEvent.start.valueOf()),
                                time: Number(calEvent.start.valueOf()),
                                title: title
                            });
                        }
                    }
                });
            }, 100);
            scope.addEventSource = function (source) {
                setTimeout(function () {
                    scope.loading = false;
                    Util.apply(scope);
                    element.fullCalendar('addEventSource', source);
                }, 100);
            }
        }
    })
    .directive('calendar2', function ($state, Util, OutpatientsAddItem, OutpatientsCancelItem) {
        return function (scope, element) {
            var array = [];
            setTimeout(function () {
                element.fullCalendar({
                    header: {
                        left: 'title',
                        center: '',
                        right: 'today prev,next'
                    },
                    views: {
                        customAgendaWeek: {
                            type: 'agendaWeek',
                            axisFormat: 'a',
                            slotDuration: '05:00',
                            minTime: '09:00'
                        }
                    },
                    firstDay: 0,
                    businessHours: {
                        start: '00:00',
                        end: '23:59'
                    },
                    height: 'auto',
                    eventLimit: true,
                    defaultView: 'customAgendaWeek',
                    selectable: true,
                    select: function (start, end) {
                        if (!_.find(array, function (value) {
                                return value.start == start.format('YYYY-MM-DD HH:mm:ss');
                            })) {
                            updateEvent(start, end.diff(start, 'hours') == 24);
                            element.fullCalendar('renderEvent', {
                                title: '√',
                                start: start,
                                end: end
                            }, true);
                            OutpatientsAddItem.get({
                                doctorId: scope.userId,
                                tenantId: 1,
                                timeType: {
                                    '00': 'allDay',
                                    '09': 'morning',
                                    '14': 'afternoon',
                                    '19': 'evening'
                                }[start.format('HH')],
                                date: start.startOf('days').valueOf()
                            });
                        }
                    },
                    eventRender: function (event, element) {
                        if (!_.find(array, function (value) {
                                return value.id == event._id;
                            })) {
                            array.push({
                                id: event._id,
                                start: event.start.format('YYYY-MM-DD HH:mm:ss'),
                                allDay: event.allDay
                            });
                        }
                        element.find('.fc-time').hide();
                        element.find('.fc-title').attr('title', event.title);
                        if (event.allDay) {
                            element.find('.fc-title').css({'line-height': '36px'});
                        } else {
                            element.find('.fc-title').css({'line-height': '18px'});
                        }
                    },
                    eventClick: function (calEvent) {
                        element.fullCalendar('removeEvents', calEvent._id);
                        OutpatientsCancelItem.get({itemId: calEvent._id});
                        array = _.filter(array, function (value) {
                            return value.start != calEvent.start.format('YYYY-MM-DD HH:mm:ss');
                        });
                    }
                });
            }, 100);
            function updateEvent(start, status) {
                _.each(_.filter(array, function (value) {
                    return (status ? !value.allDay : value.allDay) && value.start.indexOf(start.format('YYYY-MM-DD')) != -1;
                }), function (value) {
                    element.fullCalendar('removeEvents', value.id);
                    OutpatientsCancelItem.get({itemId: value.id});
                });
                array = _.filter(array, function (value) {
                    return !((status ? !value.allDay : value.allDay) && value.start.indexOf(start.format('YYYY-MM-DD')) != -1);
                });
            }
            scope.addEventSource = function (source) {
                setTimeout(function () {
                    scope.loading = false;
                    Util.apply(scope);
                    element.fullCalendar('addEventSource', source);
                }, 100);
            }
        }
    })
    .directive('calendar3', function ($state, Util, PlusManagement) {
        return function (scope, element, attrs) {
            var array = [];
            setTimeout(function () {
                element.fullCalendar({
                    header: {
                        left: 'title',
                        center: '',
                        right: 'today prev,next'
                    },
                    views: {
                        customAgendaWeek: {
                            type: 'agendaWeek',
                            axisFormat: 'a HH:mm',
                            slotDuration: '01:00',
                            minTime: '08:00'
                        }
                    },
                    firstDay: 0,
                    businessHours: {
                        start: '00:00',
                        end: '23:59'
                    },
                    height: 'auto',
                    eventLimit: true,
                    defaultView: 'customAgendaWeek',
                    allDaySlot: false,
                    selectable: true,
                    select: function (start, end) {
                        var title = prompt('');
                            scheduleName = start.format('H') < 13 ? '上午' : (start.format('H') < 19 ? '下午' : '晚上');
                        if (title) {
                            element.fullCalendar('renderEvent', {
                                title: title,
                                start: start,
                                end: end
                            }, true);
                            PlusManagement.save({type: 'add'}, {
                                date: start.format('YYYY-MM-DD'),
                                scheduleName: scheduleName,
                                plusNum: title,
                                time: start.format('HH:mm') + '-' + start.add(1, 'hours').format('HH:mm')
                            });
                        }
                    },
                    eventRender: function (event, element) {

                        element.find('.fc-time').hide();
                        element.find('.fc-title').attr('title', event.title);
                        element.find('.fc-title').css({'line-height': '18px'});
                    },
                    eventClick: function (calEvent) {

                        var title = prompt('', calEvent.title);
                        if (title) {
                            calEvent.title = title;
                            element.fullCalendar('updateEvent', calEvent);
                            PlusManagement.save({type: 'modify'}, {
                                scheduleId: calEvent._id,
                                plusNum: title
                            });
                        }
                    }
                });
            }, 100);
            scope.addEventSource = function (source) {
                setTimeout(function () {
                    scope.loading = false;
                    Util.apply(scope);
                    element.fullCalendar('addEventSource', source);
                }, 100);
            }
        }
    })
    .directive('calendar4', function ($state, Util, AgendaAddItem, AgendaUpdateItem) {
        return function (scope, element, attrs) {
            var array = [];
            setTimeout(function () {
                element.fullCalendar({
                    header: {
                        left: 'title',
                        center: '',
                        right: 'today prev,next'
                    },
                    views: {
                        customAgendaWeek: {
                            type: 'agendaWeek',
                            axisFormat: 'a HH:mm',
                            slotDuration: '01:00',
                            minTime: '08:00'
                        }
                    },
                    firstDay: 0,
                    businessHours: {
                        start: '00:00',
                        end: '23:59'
                    },
                    height: 'auto',
                    eventLimit: true,
                    defaultView: 'customAgendaWeek',
                    allDaySlot: false,
                    selectable: true,
                    select: function (start, end) {
                        var title = prompt('');
                        scheduleName = start.format('H') < 13 ? '上午' : (start.format('H') < 19 ? '下午' : '晚上');
                        if (title) {
                            element.fullCalendar('renderEvent', {
                                title: title,
                                start: start,
                                end: end
                            }, true);
                            AgendaAddItem.get({
                                doctorId: scope.userId,
                                tenantId: 1,
                                date: Number(start.valueOf()),
                                time: Number(start.valueOf()),
                                title: title
                            });
                        }
                    },
                    eventRender: function (event, element) {                    	
                        element.find('.fc-time').hide();
                        element.find('.fc-title').attr('title', event.title);
                        element.find('.fc-title').css({'line-height': '18px'});
                    },
                    eventClick: function (calEvent) {
                    	var title = prompt('', calEvent.title);
                        if (title) {
                            calEvent.title = title;
                            element.fullCalendar('updateEvent', calEvent);                            
                            AgendaUpdateItem.get({
                                itemId: calEvent._id,
                                date: Number(calEvent.start.valueOf()),
                                time: Number(calEvent.start.valueOf()),
                                title: title
                            });
                        }
                    }
                });
            }, 100);
            scope.addEventSource = function (source) {
                setTimeout(function () {
                    scope.loading = false;
                    Util.apply(scope);
                    element.fullCalendar('addEventSource', source);
                }, 100);
            }
        }
    })
    .directive('clockpicker', function () {
        return function (scope, element) {
            element
                .prop('readonly', true)
                .css({background: '#fff', cursor: 'pointer'});
            element.clockpicker({
                autoclose: true
            });
        }
    })
    .directive('datepicker', function () {
        return function (scope, element, attrs) {
            element
                .prop('readonly', true)
                .css({background: '#fff', cursor: 'pointer'})
                .datepicker({
                    autoclose: true,
                    //endDate: moment().format('YYYY-MM-DD'),
                    format: 'yyyy-mm-dd',
                    language: 'zh-CN'
                });
            scope.$watch(attrs.ngModel, function (newValue, oldValue) {
                element.datepicker('update', newValue || oldValue);
            });
        }
    })
    .directive('placeholder', function () {
        return function (scope, element) {
            //element.placeholder();
        }
    })
    .directive('scrollbar', function () {
        return function (scope, element, attrs) {
            element.mCustomScrollbar({
                axis: attrs.axis,
                theme: 'minimal-dark'
            });
            element.css({position: 'absolute'});
        }
    })
    .directive('select2', function () {
        return function (scope, element, attrs) {
            element.select2({
                language: 'zh-CN',
                placeholder: attrs.placeholder || ''
            });
            setTimeout(function () {
                element.val(0).trigger('change');
            }, 100);
        }
    })
    .directive('slick', function () {
        return function (scope, element, attrs) {
            setTimeout(function () {
                scope.$watch('index', function (value) {
                    if (value == 3) {
                        $('.slider-for').slick({
                            arrows: false,
                            asNavFor: '.slider-nav',
                            fade: true
                        });
                        $('.slider-nav').slick({
                            asNavFor: '.slider-for',
                            centerMode: true,
                            dots: true,
                            slidesToShow: 3,
                            slidesToScroll: 1
                        });
                    }
                });
            }, 100);
        }
    })
    .directive('slick2', function () {
        return function (scope, element) {
            element.slick({
                arrows: false,
                autoplay: true,
                autoplaySpeed: 2000,
                dots: true
            });
        }
    })
    .directive('spin', function () {
        return {
            template: '<div></div>',
            replace: true,
            link: function (scope, element, attrs) {
                var opts = {
                        lines: Number(attrs.lines) || 10,
                        length: Number(attrs.length) || 10,
                        width: Number(attrs.width) || 5,
                        radius: Number(attrs.radius) || 10,
                        corners: 1,
                        rotate: 0,
                        direction: 1,
                        color: '#000',
                        speed: 1,
                        trail: 20,
                        shadow: true,
                        hwaccel: false,
                        className: 'spinner',
                        zIndex: 2e9,
                        top: '50%',
                        left: '50%'
                    },
                    spinner = new Spinner(opts).spin(element[0]);
                element
                    .width((opts.length + opts.width + opts.radius) * 2)
                    .height((opts.length + opts.width + opts.radius) * 2);
                element.css({
                    position: 'fixed',
                    top: '50%',
                    left: '50%',
                    marginTop: element.width() / 2 * -1,
                    marginLeft: element.width() / 2 * -1,
                    zIndex: 999999,
                    overflow: 'hidden'
                });
            }
        };
    })
    .directive('chartBloodpressure', function () {
        return {
            template: '<div></div>',
            replace: true,
            link: function (scope, element, attr) {
                var chart = new Highcharts.Chart({
                    chart: {
                        width: 730,
                        backgroundColor: 'transparent',
                        borderWidth: 0,
                        plotBackgroundColor: 'white',
                        plotBorderWidth: 0,
                        plotShadow: false,
                        renderTo: element[0],
                        type: attr.type
                    },
                    legend: {
                        verticalAlign: 'bottom',
                        borderWidth: 0
                    },
                    series: [{
                        name: '收缩压',
                        data: []
                    }, {
                        name: '舒张压',
                        data: []
                    }, {
                        name: '心率',
                        data: []
                    }],
                    title: {
                        text: '血压趋势图',
                        x: -20
                    },
                    tooltip: {
                        shared: true
                        //valueSuffix: ''
                    },
                    xAxis: {
                        type: 'datetime',
                        labels: {
                            formatter: function () {
                                return Highcharts.dateFormat('%m/%d', this.value);
                            }
                        }
                    },
                    yAxis: {
                        title: ''
                    }
                });
                scope.$watch('chartList', function (value) {
                    if (value && scope.type.name == '血压') {
                        _.each(scope.type.conceptIds, function (item, index) {
                            chart.series[index].setData(value[item.v]);
                        });
                    }
                }, true);
            }
        };
    })
    .directive('chartBloodglucose', function () {
        return {
            template: '<div></div>',
            replace: true,
            link: function (scope, element, attr) {
                var chart = new Highcharts.Chart({
                    chart: {
                        width: 730,
                        backgroundColor: 'transparent',
                        borderWidth: 0,
                        plotBackgroundColor: 'white',
                        plotBorderWidth: 0,
                        plotShadow: false,
                        renderTo: element[0],
                        type: attr.type
                    },
                    legend: {
                        verticalAlign: 'bottom',
                        borderWidth: 0
                    },
                    series: [{
                        name: '空腹血糖',
                        data: []
                    }, {
                        name: '餐后血糖',
                        data: []
                    }],
                    title: {
                        text: '血糖趋势图',
                        x: -20
                    },
                    tooltip: {
                        shared: true
                        //valueSuffix: ''
                    },
                    xAxis: {
                        type: 'datetime',
                        labels: {
                            formatter: function () {
                                return Highcharts.dateFormat('%m/%d', this.value);
                            }
                        }
                    },
                    yAxis: {
                        title: ''
                    }
                });
                scope.$watch('chartList', function (value) {
                    if (value && scope.type.name == '血糖') {
                        _.each(scope.type.conceptIds, function (item, index) {
                            if (item.v == "dataAttr") {
                                chart.series[index].setData(value[11]);
                            } else {
                                chart.series[index].setData(value[item.v]);
                            }
                        });
                    }
                }, true);
            }
        };
    })
    .directive('chartBloodoxygen', function () {
        return {
            template: '<div></div>',
            replace: true,
            link: function (scope, element, attr) {
                var chart = new Highcharts.Chart({
                    chart: {
                        width: 730,
                        backgroundColor: 'transparent',
                        borderWidth: 0,
                        plotBackgroundColor: 'white',
                        plotBorderWidth: 0,
                        plotShadow: false,
                        renderTo: element[0],
                        type: attr.type
                    },
                    legend: {
                        verticalAlign: 'bottom',
                        borderWidth: 0
                    },
                    series: [{
                        name: '血氧浓度',
                        data: []
                    }, {
                        name: '心跳',
                        data: []
                    }],
                    title: {
                        text: '血氧趋势图',
                        x: -20
                    },
                    tooltip: {
                        shared: true
                        //valueSuffix: ''
                    },
                    xAxis: {
                        type: 'datetime',
                        labels: {
                            formatter: function () {
                                return Highcharts.dateFormat('%m/%d', this.value);
                            }
                        }
                    },
                    yAxis: {
                        title: ''
                    }
                });
                scope.$watch('chartList', function (value) {
                    if (value && scope.type.name == '血氧') {
                        _.each(scope.type.conceptIds, function (item, index) {
                            chart.series[index].setData(value[item.v]);
                        });
                    }
                }, true);
            }
        };
    })
    .directive('chartFat', function () {
        return {
            template: '<div></div>',
            replace: true,
            link: function (scope, element, attr) {
                var chart = new Highcharts.Chart({
                    chart: {
                        width: 730,
                        backgroundColor: 'transparent',
                        borderWidth: 0,
                        plotBackgroundColor: 'white',
                        plotBorderWidth: 0,
                        plotShadow: false,
                        renderTo: element[0],
                        type: attr.type
                    },
                    legend: {
                        verticalAlign: 'bottom',
                        borderWidth: 0
                    },
                    series: [{
                        name: '体脂',
                        data: []
                    }, {
                        name: 'BMI',
                        data: []
                    }, {
                        name: '内脏脂肪等级',
                        data: []
                    }, {
                        name: '基础代谢',
                        data: []
                    }, {
                        name: '水分',
                        data: []
                    }, {
                        name: '骨量',
                        data: []
                    }, {
                        name: '肌肉',
                        data: []
                    }],
                    title: {
                        text: '体脂趋势图',
                        x: -20
                    },
                    xAxis: {
                        type: 'datetime',
                        labels: {
                            formatter: function () {
                                return Highcharts.dateFormat('%m/%d', this.value);
                            }
                        }
                    },
                    yAxis: {
                        title: ''
                    }
                });
                scope.$watch('chartList', function (value) {
                    if (value && scope.type.name == '体脂') {
                        _.each(scope.type.conceptIds, function (item, index) {
                            chart.series[index].setData(value[item.v]);
                        });
                    }
                }, true);
            }
        };
    })
    .directive('chartTemperature', function () {
        return {
            template: '<div></div>',
            replace: true,
            link: function (scope, element, attr) {
                var chart = new Highcharts.Chart({
                    chart: {
                        width: 730,
                        backgroundColor: 'transparent',
                        borderWidth: 0,
                        plotBackgroundColor: 'white',
                        plotBorderWidth: 0,
                        plotShadow: false,
                        renderTo: element[0],
                        type: attr.type
                    },
                    legend: {
                        verticalAlign: 'bottom',
                        borderWidth: 0
                    },
                    series: [{
                        name: '体温',
                        data: []
                    }],
                    title: {
                        text: '体温趋势图',
                        x: -20
                    },
                    xAxis: {
                        type: 'datetime',
                        labels: {
                            formatter: function () {
                                return Highcharts.dateFormat('%m/%d', this.value);
                            }
                        }
                    },
                    yAxis: {
                        title: ''
                    }
                });
                scope.$watch('chartList', function (value) {
                    if (value && scope.type.name == '体温') {
                        _.each(scope.type.conceptIds, function (item, index) {
                            chart.series[index].setData(value[item.v]);
                        });
                    }
                }, true);
            }
        };
    })
    .directive('chartWeight', function () {
        return {
            template: '<div></div>',
            replace: true,
            link: function (scope, element, attr) {
                var chart = new Highcharts.Chart({
                    chart: {
                        width: 730,
                        backgroundColor: 'transparent',
                        borderWidth: 0,
                        plotBackgroundColor: 'white',
                        plotBorderWidth: 0,
                        plotShadow: false,
                        renderTo: element[0],
                        type: attr.type
                    },
                    legend: {
                        verticalAlign: 'bottom',
                        borderWidth: 0
                    },
                    series: [{
                        name: '体重',
                        data: []
                    }],
                    title: {
                        text: '体重趋势图',
                        x: -20
                    },
                    xAxis: {
                        type: 'datetime',
                        labels: {
                            formatter: function () {
                                return Highcharts.dateFormat('%m/%d', this.value);
                            }
                        }
                    },
                    yAxis: {
                        title: ''
                    }
                });
                scope.$watch('chartList', function (value) {
                    if (value && scope.type.name == '体重') {
                        _.each(scope.type.conceptIds, function (item, index) {
                            chart.series[index].setData(value[item.v]);
                        });
                    }
                }, true);
            }
        };
    })
    .directive('chartBloodlipids', function () {
        return {
            template: '<div></div>',
            replace: true,
            link: function (scope, element, attr) {
                var chart = new Highcharts.Chart({
                    chart: {
                        width: 730,
                        backgroundColor: 'transparent',
                        borderWidth: 0,
                        plotBackgroundColor: 'white',
                        plotBorderWidth: 0,
                        plotShadow: false,
                        renderTo: element[0],
                        type: attr.type
                    },
                    legend: {
                        verticalAlign: 'bottom',
                        borderWidth: 0
                    },
                    series: [{
                        name: '甘油三酯',
                        data: []
                    }, {
                        name: '总胆固醇',
                        data: []
                    }, {
                        name: '低密度脂蛋白胆固醇',
                        data: []
                    }, {
                        name: '高密度脂蛋白胆固醇',
                        data: []
                    }],
                    title: {
                        text: '血脂趋势图',
                        x: -20
                    },
                    xAxis: {
                        type: 'datetime',
                        labels: {
                            formatter: function () {
                                return Highcharts.dateFormat('%m/%d', this.value);
                            }
                        }
                    },
                    yAxis: {
                        title: ''
                    }
                });
                scope.$watch('chartList', function (value) {
                    if (value && scope.type.name == '血脂') {
                        _.each(scope.type.conceptIds, function (item, index) {
                            chart.series[index].setData(value[item.v]);
                        });
                    }
                }, true);
            }
        };
    })
    .directive('chartSport', function () {
        return {
            template: '<div></div>',
            replace: true,
            link: function (scope, element, attr) {
                var chart = new Highcharts.Chart({
                    chart: {
                        width: 730,
                        backgroundColor: 'transparent',
                        borderWidth: 0,
                        plotBackgroundColor: 'white',
                        plotBorderWidth: 0,
                        plotShadow: false,
                        renderTo: element[0],
                        type: attr.type
                    },
                    legend: {
                        verticalAlign: 'bottom',
                        borderWidth: 0
                    },
                    series: [{
                        name: '步数',
                        data: []
                    }],
                    title: {
                        text: '步数趋势图',
                        x: -20
                    },
                    xAxis: {
                        type: 'datetime',
                        labels: {
                            formatter: function () {
                                return Highcharts.dateFormat('%m/%d', this.value);
                            }
                        }
                    },
                    yAxis: {
                        title: ''
                    }
                });
                scope.$watch('chartList', function (value) {
                    if (value && scope.type.name == '运动') {
                        _.each(scope.type.conceptIds.slice(0, 1), function (item, index) {
                            chart.series[index].setData(value[item.v]);
                        });
                    }
                }, true);
            }
        };
    })
    .directive('chartSport2', function () {
        return {
            template: '<div></div>',
            replace: true,
            link: function (scope, element, attr) {
                var chart = new Highcharts.Chart({
                    chart: {
                        width: 730,
                        backgroundColor: 'transparent',
                        borderWidth: 0,
                        plotBackgroundColor: 'white',
                        plotBorderWidth: 0,
                        plotShadow: false,
                        renderTo: element[0],
                        type: attr.type
                    },
                    colors: ['#50B432'],
                    legend: {
                        verticalAlign: 'bottom',
                        borderWidth: 0
                    },
                    series: [{
                        name: '距离',
                        data: []
                    }],
                    title: {
                        text: '距离趋势图',
                        x: -20
                    },
                    xAxis: {
                        type: 'datetime',
                        labels: {
                            formatter: function () {
                                return Highcharts.dateFormat('%m/%d', this.value);
                            }
                        }
                    },
                    yAxis: {
                        title: ''
                    }
                });
                scope.$watch('chartList', function (value) {
                    if (value && scope.type.name == '运动') {
                        _.each(scope.type.conceptIds.slice(1, 2), function (item, index) {
                            chart.series[index].setData(value[item.v]);
                        });
                    }
                }, true);
            }
        };
    })
    .directive('chartSport3', function () {
        return {
            template: '<div></div>',
            replace: true,
            link: function (scope, element, attr) {
                var chart = new Highcharts.Chart({
                    chart: {
                        width: 730,
                        backgroundColor: 'transparent',
                        borderWidth: 0,
                        plotBackgroundColor: 'white',
                        plotBorderWidth: 0,
                        plotShadow: false,
                        renderTo: element[0],
                        type: attr.type
                    },
                    colors: ['#ED561B'],
                    legend: {
                        verticalAlign: 'bottom',
                        borderWidth: 0
                    },
                    series: [{
                        name: '运动时长',
                        data: []
                    }],
                    title: {
                        text: '运动时长趋势图',
                        x: -20
                    },
                    xAxis: {
                        type: 'datetime',
                        labels: {
                            formatter: function () {
                                return Highcharts.dateFormat('%m/%d', this.value);
                            }
                        }
                    },
                    yAxis: {
                        title: ''
                    }
                });
                scope.$watch('chartList', function (value) {
                    if (value && scope.type.name == '运动') {
                        _.each(scope.type.conceptIds.slice(2, 3), function (item, index) {
                            chart.series[index].setData(value[item.v]);
                        });
                    }
                }, true);
            }
        };
    })
    .directive('chartSport4', function () {
        return {
            template: '<div></div>',
            replace: true,
            link: function (scope, element, attr) {
                var chart = new Highcharts.Chart({
                    chart: {
                        width: 730,
                        backgroundColor: 'transparent',
                        borderWidth: 0,
                        plotBackgroundColor: 'white',
                        plotBorderWidth: 0,
                        plotShadow: false,
                        renderTo: element[0],
                        type: attr.type
                    },
                    colors: ['#DDDF00'],
                    legend: {
                        verticalAlign: 'bottom',
                        borderWidth: 0
                    },
                    series: [{
                        name: '热量',
                        data: []
                    }],
                    title: {
                        text: '热量趋势图',
                        x: -20
                    },
                    xAxis: {
                        type: 'datetime',
                        labels: {
                            formatter: function () {
                                return Highcharts.dateFormat('%m/%d', this.value);
                            }
                        }
                    },
                    yAxis: {
                        title: ''
                    }
                });
                scope.$watch('chartList', function (value) {
                    if (value && scope.type.name == '运动') {
                        _.each(scope.type.conceptIds.slice(3, 4), function (item, index) {
                            chart.series[index].setData(value[item.v]);
                        });
                    }
                }, true);
            }
        };
    })
    .directive('chartAppointment', function () {
        return {
            template: '<div></div>',
            replace: true,
            link: function (scope, element, attr) {
                var data = [
                        {name: '新浪', data: []},
                        {name: '阿里健康云医院', data: []},
                        {name: '华康', data: []},
                        {name: '寻医问药', data: []}
                    ],
                    chart = null;
                chart = new Highcharts.Chart({
                    chart: {
                        width: 750,
                        backgroundColor: 'transparent',
                        borderWidth: 0,
                        plotBackgroundColor: 'transparent',
                        plotBorderWidth: 0,
                        plotShadow: false,
                        renderTo: element[0],
                        type: attr.type
                    },
                    legend: {
                        verticalAlign: 'bottom',
                        borderWidth: 0
                    },
                    series: data,
                    title: {
                        text: ''
                    },
                    tooltip: {
                        formatter: function() {
                            return '<b>'+ this.series.name +'</b><br/>'+
                                "数量"+ this.y +'</b><br/>'+'日期'+moment(this.x).format('MM-DD');
                        }
                    },
                    xAxis: {
                        type: 'datetime',
                        labels: {
                            formatter: function () {
                                return moment(this.value).format('MM-DD');
                            }
                        }
                    },
                    yAxis: {
                        title: ''
                    }
                });
                scope.$watch('chartList', function (value) {
                    if(value!=null) {
                        _.each(scope.sourceType, function (item, index) {
                            chart.series[index].setData(value[item]);
                        });
                    }
                }, true);
            }
        };
    })
    .directive('chartConsult', function () {
        return {
            template: '<div></div>',
            replace: true,
            link: function (scope, element, attr) {
                var data = [
                        {name: '新浪', data: []},
                        {name: '阿里健康云医院', data: []},
                        {name: '华康', data: []},
                        {name: '寻医问药', data: []}
                    ],
                    chart = null;
                chart = new Highcharts.Chart({
                    chart: {
                        width: 750,
                        backgroundColor: 'transparent',
                        borderWidth: 0,
                        plotBackgroundColor: 'transparent',
                        plotBorderWidth: 0,
                        plotShadow: false,
                        renderTo: element[0],
                        type: attr.type
                    },
                    legend: {
                        verticalAlign: 'bottom',
                        borderWidth: 0
                    },
                    series: data,
                    title: {
                        text: ''
                    },
                    tooltip: {
                        formatter: function() {
                            return '<b>'+ this.series.name +'</b><br/>'+
                                "数量"+ this.y +'</b><br/>'+'日期'+moment(this.x).format('MM-DD');
                        }
                    },
                    xAxis: {
                        type: 'datetime',
                        labels: {
                            formatter: function () {
                                return moment(this.value).format('MM-DD')
                                //return Highcharts.dateFormat('%m/%d', this.value);
                            }
                        }
                    },
                    yAxis: {
                        title: ''
                    }
                });
                scope.$watch('chartList', function (value) {
                    if(value!=null) {
                        _.each(scope.sourceType, function (item, index) {
                            chart.series[index].setData(value[item]);
                        });
                    }
                }, true);
            }
        };
    });