define(['appUmbrella','jquery'], function (app,$) {
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
                    // Instantiate the controller
                    var swipeController = new SwipeableCardController({
                    });

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
