app.controller("dataListController", function($scope, $window) {
    $scope.list = [];
    var curGuid = 0;
    var EDIT_MODE = 1;
    var ADD_MODE = -1;
    $scope.isShow = false;

    function createGuid() {
        return ++curGuid;
    }
    //删除
    $scope.remove = function(index) {
        if ($window.confirm("确定要删除该条信息?")) {
            $scope.list.splice(index, 1);
        }
    }
    //添加
    $scope.add = function() {
        $scope.$broadcast("controller.add");
    }
    //编辑
    $scope.edit = function(index) {
        $scope.$broadcast("controller.edit", {
            formData: $scope.list[index],
            index: index
        });
    }
    //添加的点击事件
    $scope.$on("controller.add", function(event, data) {
        $scope.isShow = true;
        $scope.mode = ADD_MODE;
    });
    //编辑
    $scope.$on("controller.edit", function(event, e) {
        $scope.isShow = true;
        $scope.formData.text = e.formData.text;
        $scope.formData.des = e.formData.des;
        $scope.mode = e.index;
    });
    //添加数据
    $scope.$on("controller.addData", function(event, e) {
        var formData = e.formData;
        $scope.list.push(formData);
    });
    //编辑数据
    $scope.$on("controller.editData", function(event, e) {
        var formData = e.formData;
        $scope.list[e.index] = formData;
    });
    //空数组
    $scope.formData = {
        text: "",
        des: ""
    };
    //提交的功能
    $scope.submit = function() {
        var data = {
            formData: {
                text: $scope.formData.text,
                des: $scope.formData.des
            }
        };
        //判断编辑还是添加的
        if ($scope.mode != ADD_MODE) {
            data.index = $scope.mode;
            $scope.$emit("controller.editData", data);
        } else {
            $scope.$emit("controller.addData", data);
        }
        $scope.close(true);
    }
    //关
    $scope.close = function(immediate) {
        if (!immediate) {
            if ($scope.formData.text || $scope.formData.des) {
                if (!$window.confirm("不保留已更改数据吗？")) {
                    return;
                }
            }
        }
        $scope.formData.text = "";
        $scope.isShow = false;
    }



    $scope.list1 = [];
    var curGuid1 = 0;
    var EDIT_MODE1 = 1;
    var ADD_MODE1 = -1;
    $scope.isShow1 = false;

    function createGuid1() {
        return ++curGuid1;
    }
    //删除
    $scope.remove1 = function(index) {
        if ($window.confirm("确定要删除该条信息?")) {
            $scope.list1.splice(index, 1);
        }
    }
    //添加
    $scope.add1 = function() {
        $scope.$broadcast("controller.add1");
    }
    //编辑
    $scope.edit1 = function(index) {
        $scope.$broadcast("controller.edit1", {
            formData1: $scope.list1[index],
            index: index
        });
    }
    //添加的点击事件
    $scope.$on("controller.add1", function(event, data) {
        $scope.isShow1 = true;
        $scope.mode = ADD_MODE1;
    });
    //编辑
    $scope.$on("controller.edit1", function(event, e) {
        $scope.isShow1 = true;
        $scope.formData1.text = e.formData1.text;
        $scope.formData1.des = e.formData1.des;
        $scope.mode = e.index;
    });
    //添加数据
    $scope.$on("controller.addData1", function(event, e) {
        var formData1 = e.formData1;
        $scope.list1.push(formData1);
    });
    //编辑数据
    $scope.$on("controller.editData1", function(event, e) {
        var formData1 = e.formData1;
        $scope.list1[e.index] = formData1;
    });
    //空数组
    $scope.formData1 = {
        des: ""
    };
    //提交的功能
    $scope.submit1 = function() {
        var data = {
            formData1: {
                des: $scope.formData1.des
            }
        };
        //判断编辑还是添加的
        if ($scope.mode != ADD_MODE1) {
            data.index = $scope.mode;
            $scope.$emit("controller.editData1", data);
        } else {
            $scope.$emit("controller.addData1", data);
        }
        $scope.close1(true);
    }
    //关
    $scope.close1 = function(immediate) {
        if (!immediate) {
            if ($scope.formData1.text || $scope.formData1.des) {
                if (!$window.confirm("不保留已更改数据吗？")) {
                    return;
                }
            }
        }
        $scope.formData1.des = "";
        $scope.isShow1 = false;
    }
});

