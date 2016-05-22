angular.module('directives2', [])
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
