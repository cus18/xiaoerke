define(['appUmbrella2','jquery'], function (app,$) {
    app
        .directive('bdfHead', ['$rootScope','$state','checkUserOrder',
            function ($rootScope,$state,checkUserOrder) {
                return {
                    restrict: 'EAC',
                    replace: true,
                    template: '<div class="head">' +
                    '<div class="left" ng-click="appointmentFirst()"><span class="city">首页</span> </div>' +
                    '<h1 class="title" >{{title}}</h1>' +
                    '<div class="right"><a class= my" ui-sref="myselfFirst">' +
                    '<img src="images/me1.png" width="22" height="22" ng-if="!haveOrder">' +
                    '<img src="images/me2.png" width="22" height="22" ng-if="haveOrder"> </a> ' +
                    '</div> </div>',
                    link: function(scope,ele,attrs) {
                        scope.appointmentFirst = function(){
                            window.location.href = "ap";
                        }
                        scope.goBack = function(){
                            history.back();
                        }
                        scope.haveOrder = false;
                        checkUserOrder.save({unBindUserPhoneNum:$rootScope.unBindUserPhoneNum},function(data){
                            if(data.haveOrder){
                                scope.haveOrder = data.haveOrder
                            }
                        });
                    }
                }
            }])
        .directive('swipeCards', ['$rootScope', function($rootScope) {
            return {
                restrict: 'E',
                template: '<div class="swipe-cards" ng-transclude></div>',
                replace: true,
                transclude: true,
                scope: {},
                controller: function($scope, $element) {
                    var SwipeableCardController = ionic.controllers.ViewController.inherit({
                        initialize: function(opts) {
                            this.cards = [];
                            // 根据opts进行初始化
                        },
                        pushCard: function(card) {
                            this.cards.push(card);
                            // 显示卡片
                        },
                        popCard: function() {
                            var card = this.cards.pop();
                            // 卡片消失的动画
                        }
                    })

                    // Instantiate the controller
                    var swipeController = new SwipeableCardController({});

                    // We add a root scope event listener to facilitate interacting with the
                    // directive incase of no direct scope access
                    $rootScope.$on('swipeCard.pop', function(isAnimated) {
                        swipeController.popCard(isAnimated);
                    });

                    // return the object so it is accessible to child
                    // directives that 'require' this directive as a parent.
                    return swipeController;
                }
            }
        }])
        .directive('swipeCard', ['$timeout', function($timeout) {
            return {
                restrict: 'E',
                template: '<div class="swipe-card" ng-transclude></div>',

                // Requiring the swipeCards directive makes the controller available
                // in the linking function
                require: '^swipeCards',
                replace: true,
                transclude: true,
                scope: {
                    onSwipe: '&',
                    onDestroy: '&'
                },
                compile: function(element, attr) {
                    return function($scope, $element, $attr, swipeCards) {
                        var el = $element[0];

                        var SwipeableCardView = ionic.views.View.inherit({
                            initialize: function(opts) {
                                // 保存卡片元素
                                this.el = opts.el;
                                this.bindEvents();
                            },
                            bindEvents: function() {
                                var self = this;

                                ionic.onGesture('dragstart', function(e) {
                                    // 拖动过程开始
                                }, this.el);

                                ionic.onGesture('drag', function(e) {
                                    // 拖动的过程
                                    window.rAF(function() {
                                        self._doDrag(e);
                                    });
                                }, this.el);

                                ionic.onGesture('dragend', function(e) {
                                    // 拖动过程结束
                                }, this.el);
                            },
                            _doDrag: function(e) {
                                // 在拖动的过程中计算出我们已经拖多远
                                var o = e.gesture.deltaY / 3;

                                // Get the angle of rotation based on the
                                // drag distance and our distance from the
                                // center of the card (computed in dragstart),
                                // and the side of the card we are dragging from
                                this.rotationAngle = Math.atan(o/this.touchDistance) * this.rotationDirection;

                                // Don't rotate if dragging up
                                if(e.gesture.deltaY < 0) {
                                    this.rotationAngle = 0;
                                }

                                // Update the y position of this view
                                this.y = this.startY + (e.gesture.deltaY * 0.4);

                                // Apply the CSS transformation to the card,
                                // translating it up or down, and rotating
                                // it based on the computed angle
                                this.el.style[ionic.CSS.TRANSFORM] = 'translate3d(' + this.x + 'px, ' + this.y  + 'px, 0) rotate(' + (this.rotationAngle || 0) + 'rad)';
                            }
                        });

                        // Instantiate our card view
                        var swipeableCard = new SwipeableCardView({
                            el: el,
                            onSwipe: function() {
                                $timeout(function() {
                                    $scope.onSwipe();
                                });
                            },
                            onDestroy: function() {
                                $timeout(function() {
                                    $scope.onDestroy();
                                });
                            },
                        });

                        // Make the card available to the parent scope, not necessary
                        // but makes it easier to interact with (similar to iOS exposing
                        // parent controllers and views dynamically to children)
                        $scope.$parent.swipeCard = swipeableCard;

                        // We can push a new card onto the controller card stack, animating it in
                        swipeCards.pushCard(swipeableCard);
                    }
                }
            }
        }])

})

