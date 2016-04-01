angular.module('controllers', ['ionic']).controller('nutritionEffectCtrl', [
    '$scope','$state','$stateParams','GetEvaluate',
    function ($scope,$state,$stateParams,GetEvaluate) {

        $scope.scrollNum=0;
        //harm：危害 suggest：建议 trait：特点
        var oilSalt = [
            {harm:"食用油是人体脂肪的主要来源。脂肪是人体三大产能营养素之一，要为人体提供能量，也为脂溶性维生素的吸收提供必需条件，同时脂肪还是各个脏器的支撑和保护者。缺少脂肪，人体正常的生理代谢就不能进行，还会受疾病的困扰。",
             suggest:"宝宝每天摄入10-20g食用油，也就是1-2汤匙。在选择食用油时，要注意以下几点：①种类：不同种类的植物油交替食用，如橄榄油、紫苏油、玉米油、豆油等，避免长期选用同一种植物油，以平衡各类脂肪酸的摄入量。②小包装：选择小包装的食用油以减少油脂的氧化酸败程度。③品牌 ：尽量选择大品牌，质量更有保证。"
             },
            {suggest:"继续保持清淡饮食的好习惯，在烹调方式上尽量选择蒸、煮、炖的方式，炒菜时少放油和盐，让宝宝从小适应淡口味的菜肴，预防偏食挑食的发生。"},
            {harm:"食用油主要为人体提供脂肪，但脂肪摄入过多是引起肥胖、高血脂、动脉粥样硬化等多种慢性疾病的危险因素之一。如果宝宝从小就高油高盐饮食，养成重口味的习惯，长大后很容易养成挑食、偏食的习惯，患各种慢性病的概率也会升高。",
             suggest:"宝宝每天摄入10-20g食用油，也就是1-2汤匙即可。1-3岁的幼儿还是尽量少吃盐甚至不吃盐，让宝宝养成清淡饮食的好习惯，每日摄入食盐量不超过2g。烹调方式上尽量选择蒸、煮、炖的方式，炒菜时少放油和盐，让宝宝从小适应淡口味的菜肴，预防偏食挑食的发生。"
             },
            {trait:"食用油是提供人们所需脂肪的重要来源，包括动物油和植物油，有利于脂溶性维生素的消化吸收。动物油含脂肪90%左右，还含有胆固醇；植物油一般含脂肪99%以上，不含胆固醇，且是维生素E的首要来源。食盐的主要成分是氯化钠，可以改善食物的口味，而且钠元素也是人体内不可缺少的一种化学元素，可以调节渗透压，增强神经肌肉兴奋性，维持酸碱平衡和血压正常功能。大多数天然食物中都含有钠，所以从食盐中所需的钠并不多，因此建议日常膳食应清淡少油少盐"}
        ];
        var vegetables = [
            {harm:"蔬菜类食物，尤其是深色蔬菜缺乏的话会导致很多维生素、矿物质和植物化学物质的缺乏，从而造成各种营养素缺乏症的出现，不利于宝宝的健康成长。",
                suggest:"宝宝每天应摄入100-200g的蔬菜类食物，尤其是深色蔬菜，常见的深绿色蔬菜有：菠菜、油菜、芹菜、西兰花、茼蒿等；常见的红色、橘红色蔬菜有：西红柿、胡萝卜、南瓜等；常见的紫红色蔬菜有：紫甘蓝、红苋菜等。"
            },
            {suggest:"宝宝多吃些深色蔬菜，深色蔬菜的营养价值优于浅色蔬菜。常见的深绿色蔬菜有：菠菜、油菜、芹菜、西兰花、茼蒿等；常见的红色、橘红色蔬菜有：西红柿、胡萝卜、南瓜等；常见的紫红色蔬菜有：紫甘蓝、红苋菜等。"},
            {harm:"蔬菜摄入过多可能会影响其它种类食物的摄入哦，导致膳食不均衡。",
                suggest:"宝宝可以适当减少蔬菜的摄入量，以免宝宝的胃容量都被蔬菜给占据了，不愿进食其他食物了，要留一些地方给谷薯类、水果、鱼禽肉蛋奶类哦。合理的搭配膳食才更有利于宝宝健康成长哦。"
            },
            {trait:"蔬菜类食物主要为人体提供维生素、矿物质、膳食纤维和植物化学物质，水分多，能量低，多数蔬菜含水量在90%以上。一般深色蔬菜的胡萝卜素、核黄素和维生素C含量较浅色蔬菜高，而且含有更多的植物化学物质。叶菜的营养价值一般高于根茎部和瓜菜。"}
        ];
        var milletandpotato = [
            {harm:"谷薯类摄入不足会导致宝宝大脑供能不足，造成理解、记忆能力下降，严重的话会出现头晕、昏迷哦。另外，谷薯类摄入不足容易造成膳食纤维的缺乏，导致便秘、腹胀、消化不良等问题。",
             suggest:"宝宝每天摄入70-100g的谷薯类能够充分满足碳水化合物的需要量，保证脑组织有足够的葡萄糖供能。主食要注意粗细搭配，加入1/4-1/3的粗杂粮或薯类，以增加膳食纤维及B族维生素的含量，更有利于宝宝的健康哦。"
            },
            {suggest:"宝宝的主食要注意粗细搭配，加入1/4-1/3的粗杂粮或薯类，以增加膳食纤维及B族维生素的含量，更有利于宝宝的健康哦。另外，宝宝的饮食中不仅要摄入足够的谷薯类食物，还要注意其他蔬菜、水果、鱼禽肉蛋奶的摄入量哦，这样才能让宝宝从小养成均衡膳食的好习惯。"},
            {harm:"谷薯类食物摄入过多会导致宝宝能量摄入过多，这些过多的能量如果不能通过运动等方式消耗掉的话，会在体内转变成脂肪囤积起来，长此以往，宝宝就变成小胖墩啦。",
             suggest:"宝宝每天摄入70-100g的谷薯类就能够充分满足碳水化合物的需要量，保证脑组织有足够的葡萄糖供能。主食要注意粗细搭配，加入1/4-1/3的粗杂粮或薯类，以增加膳食纤维及B族维生素的含量，更有利于宝宝的健康哦。"
            },
            {trait:"谷薯类食物主要为人体提供碳水化合物、B族维生素和膳食纤维。谷类食物是人体能量的主要来源，也是最经济的能源物质。选择谷薯类时要注意粗细搭配，这样才能摄入足够的B族维生素和膳食纤维哦。"}
        ];
        var meat = [
            {harm:"水果类食物摄入不足容易造成维生素C、维生素E、果胶、有机酸和膳食纤维的缺乏，例如维生素C缺乏容易造成牙龈出血，果胶和膳食纤维的缺乏容易导致宝宝便秘的发生。",
                suggest:"宝宝每天应摄入100-200g水果类食物，如水果中维生素C含量最高的鲜枣、草莓、猕猴桃等，让宝宝有超强的抗氧化能力和免疫力。"
            },
            {suggest:"挑选水果时，建议宝妈们尽量选择应季水果，选择维生素C含量最高的水果，如鲜枣、草莓、猕猴桃等，让宝宝有超强的抗氧化能力和免疫力，黄酮类含量较高的橙子、橘子、柠檬、葡萄柚等，能增强宝宝皮肤、肺、胃肠道及肝脏中某些酶的活力，同时还可增强宝宝对抗癌物质——维生素C的吸收能力。"},
            {harm:"宝宝可以适当减少水果的摄入量，以免宝宝的胃容量都被水果给占据了，不愿进食其他食物了，要留一些地方给谷薯类、水果、鱼禽肉蛋奶类哦。合理的搭配膳食才更有利于宝宝健康成长哦。",
                suggest:"宝宝可以适当减少水果的摄入量，以免宝宝的胃容量都被水果给占据了，不愿进食其他食物了，要留一些地方给谷薯类、水果、鱼禽肉蛋奶类哦。合理的搭配膳食才更有利于宝宝健康成长哦。"
            },
            {trait:"水果类食物主要为人体提供维生素、矿物质和膳食纤维。多数新鲜水果含水分85%-90%，能量一般比蔬菜高。水果中的有机酸如苹果酸、柠檬酸、酒石酸等能刺激人体消化液分泌，增进食欲，有利于食物的消化。"}
        ];
        var fishEggs = [
            {harm:"鱼禽肉蛋类食物摄入不足容易造成优质蛋白质的缺乏以及维生素A、维生素D、血红素铁以及ω-3系列多不饱和脂肪酸如DHA、EPA的缺乏，导致宝宝肌肉增长缓慢、缺钙、缺铁性贫血等营养不良症状的发生。",
                suggest:"宝宝每天吃一个小鸡蛋（约50g）,蛋黄中的卵磷脂有助于提高宝宝的记忆力。每天吃半两左右的肉类，补充铁和B族维生素，预防宝宝贫血。每周吃2-3次鱼虾类，尤其是海鱼，如黄花鱼、带鱼、鲈鱼等，有利于宝宝的神经系统发育。每周吃一次猪肝或鸡肝（半两左右即可），补充足够的维生素A。"
            },
            {suggest:"在动物类食品选择上建议宝宝每天吃一个小鸡蛋（约50g）,蛋黄中的卵磷脂有助于提高宝宝的记忆力。每天吃半两左右的肉类，补充铁和B族维生素，预防宝宝贫血。每周吃2-3次鱼虾类，尤其是海鱼，如黄花鱼、带鱼、鲈鱼等，有利于宝宝的神经系统发育。每周吃一次猪肝或鸡肝（半两左右即可），补充足够的维生素A。"},
            {harm:"动物性食品摄入过多容易导致脂肪、蛋白质、维生素A等营养素过剩，脂肪过多易造成能量过剩导致肥胖，蛋白质过多会加重肝肾负担，维生素A属于脂溶性维生素，长期摄入过多会引起肝脏蓄积中毒。",
                suggest:"宝宝每天吃一个小鸡蛋（约50g）,蛋黄中的卵磷脂有助于提高宝宝的记忆力。每天吃半两左右的肉类，补充铁和B族维生素，预防宝宝贫血。每周吃2-3次鱼虾类，尤其是海鱼，如黄花鱼、带鱼、鲈鱼等，有利于宝宝的神经系统发育。每周吃一次猪肝或鸡肝（半两左右即可），补充足够的维生素A。"
            },
            {trait:"鱼禽肉蛋类均属于动物性食物，是人类优质蛋白、脂类、脂溶性维生素、B族维生素和矿物质的良好来源。动物性食物中蛋白质不仅含量高，而且氨基酸组成更适合人体需要，尤其是富含赖氨酸和蛋氨酸，如与谷类或豆类食物搭配食用，可明显发挥蛋白质互补作用；但动物性食物一般都含有一定量的饱和脂肪和胆固醇，摄入过多可能增加肥胖和患心血管疾病的危险性。"}
        ];
        var milk = [
            {harm:"奶类摄入不足的话，很容易导致优质蛋白质、维生素A、B族维生素和钙的缺乏。",
                suggest:"宝宝每天摄入400-600ml的母乳或者牛奶、奶粉，如果宝宝不能摄入奶制品，可用100g鸡蛋（约2个）经适当加工来代替，如蒸蛋羹等，或者用经过适宜加工的豆制代乳品替代，以补充奶类摄入不足导致的蛋白质、钙等营养素的缺乏。"
            },
            {suggest:"宝宝母乳喂养至2岁或以上，母乳量每天600ml左右。宝宝如果是喝普通液态奶，建议将牛奶稀释，或与淀粉、蔗糖类食物调制后食用，例如在100ml的牛奶中加入50ml的水，因为普通液态奶中的蛋白质含量是母乳中的近3倍，矿物质含量也比较高，由于幼儿的肾脏功能发育尚不完善，直接喂给普通液态奶会对幼儿肾脏和肠道造成较大负担，因此，不宜直接喂给普通液态奶。如果是喝幼儿配方奶粉的话，每天50-80g即可。"},
            {harm:"奶类摄入过多的话，一方面容易造成能量摄入过多，导致儿童期肥胖，另一方面容易造成蛋白质过剩，加重宝宝肝肾负担和肠道负担。",
                suggest:"减少奶类食物的摄入量，每日400-600ml即可。过少容易造成蛋白质、钙、维生素A的缺乏，过多易导致营养过剩，加重肝肾负担和肠道负担。"
            },
            {trait:"奶类是一种营养成分齐全、组成比例适宜、易消化吸收、营养价值高的天然食品，主要提供优质蛋白质、维生素A、维生素B2和钙。母乳中蛋白质含量为1.3%，牛奶中蛋白质含量平均为3%，消化率高达90%以上，其必需氨基酸比例也符合人体需要，属于优质蛋白质。但母乳中所含钙远不如牛奶或奶粉，因此如果是纯母乳喂养的宝宝要多吃含钙高的豆制品和鱼虾类或补充适量的钙制剂。"}
        ];
        var water = [
            {harm:"幼儿活泼好动，出汗较多，另外肾脏功能还不是非常完善，容易出现缺水；宝宝缺水时可使食欲受到明显抑制，因此，应特别注意让宝宝每天均匀地足量饮水哦。",
                suggest:"宝宝每日最好直接饮水600-1000ml，科学的饮水方式是：要少量多次，不要暴饮；要定时饮水，不要等到口渴时才喝水；喝开水不喝生水；喝鲜水不喝陈水；生病情况下要根据医生的指导酌量饮水。"
            },
            {suggest:"宝宝最好的饮料是白开水。宝宝饮水时要注意，不能等到口渴时再饮水，而是要定时饮用。因为当宝宝口渴要求饮水时，身体其实已经处于轻度脱水的状态，所以家长应从小培养宝宝饮水的好习惯。最佳饮水时间是早晨和午睡起床后；在活动和洗澡游泳过程中，宝宝会失去较多的水分，此时一定要及时补充；餐前半小时至1小时适量饮水，可以使水分及时补充到全身细胞中，宝宝进餐时消化道就能分泌足够的消化液，孩子有食欲，食物也能得到充分的消化吸收。"},
            {harm:"饮水量过多，尤其是一次性饮水过多，会加重心脏、肾脏和膀胱的负担，可能引起不良后果，如果水质不佳，更对身体有危害。另外，饮水过多冲淡了血液，全身细胞的氧交换就会受到影响，脑细胞一旦缺氧，人就会变得迟钝，也会变得困倦。",
                suggest:"宝宝每日直接饮水600-1000ml，无需过多，最好的饮料是白开水。科学的饮水方式是：要少量多次，不要暴饮；要定时饮水，不要等到口渴时才喝水；喝开水不喝生水；喝鲜水不喝陈水；生病情况下要根据医生的指导酌量饮水。"
            },
            {trait:"水是膳食的重要组成部分，是一切生命必需的物质，在生命活动中发挥着重要功能。体内水的来源有饮水、食物中含的水和体内代谢产生的水。水的排出主要通过肾脏以尿液的形式排出，其次是经肺呼出、经皮肤和随粪便排出。"}
        ];



        $scope.$on('$ionicView.enter', function(){
            $scope.info = {};
            $scope.foodList = [];
            var food = {};var food1 = {};var food2 = {};var food3 = {};var food4 = {};var food5 = {};var food6 = {};
            if($stateParams.finish=="no"){
                $scope.type = "true";
                if($stateParams.type =="oilSalt"){//油类
                    $scope.info.title = "油类";
                    $scope.info.img = "http://oss-cn-beijing.aliyuncs.com/xiaoerke-article-pic/yyglFoodCharacteristicOilSalt";
                    if($stateParams.weight==""){//查看食物特点
                        getTrait();
                        $scope.foodTrait = oilSalt[3].trait;

                    }else{//根据食物量多量少的危害
                        $scope.sug = true;
                        $scope.tra = true;
                        if($stateParams.weight=="less"){
                            getLessMore("不足",oilSalt,0);
                        }else if($stateParams.weight=="just"){
                            getJust(oilSalt,1);
                        }else{
                            getLessMore("多了",oilSalt,2);
                        }
                    }

                }
                if($stateParams.type =="milk"){//奶类
                    $scope.info.title = "奶类";
                    $scope.info.img = "http://oss-cn-beijing.aliyuncs.com/xiaoerke-article-pic/yyglFoodCharacteristicMilk";
                    if($stateParams.weight==""){//查看食物特点
                        getTrait();
                        $scope.foodTrait = milk[3].trait;

                    }else{//根据食物量多量少的危害
                        $scope.sug = true;
                        $scope.tra = true;
                        if($stateParams.weight=="less"){
                            getLessMore("不足",milk,0);
                        }else if($stateParams.weight=="just"){
                            getJust(milk,1);
                        }else{
                            getLessMore("多了",milk,2);
                        }
                    }
                }
                if($stateParams.type =="fishEggs"){//鱼禽类
                    $scope.info.title = "鱼禽肉蛋类";
                    $scope.info.img = "http://oss-cn-beijing.aliyuncs.com/xiaoerke-article-pic/yyglFoodCharacteristicFishMeatEggs";
                    if($stateParams.weight==""){//查看食物特点
                        getTrait();
                        $scope.foodTrait = fishEggs[3].trait;

                    }else{//根据食物量多量少的危害
                        $scope.sug = true;
                        $scope.tra = true;
                        if($stateParams.weight=="less"){
                            getLessMore("不足",fishEggs,0);
                        }else if($stateParams.weight=="just"){
                            getJust(fishEggs,1);
                        }else{
                            getLessMore("多了",fishEggs,2);
                        }
                    }
                }
                if($stateParams.type =="meat"){//水果类
                    $scope.info.title = "水果类";
                    $scope.info.img = "http://oss-cn-beijing.aliyuncs.com/xiaoerke-article-pic/yyglFoodCharacteristicFruits";
                    if($stateParams.weight==""){//查看食物特点
                        getTrait();
                        $scope.foodTrait = meat[3].trait;

                    }else{//根据食物量多量少的危害
                        $scope.sug = true;
                        $scope.tra = true;
                        if($stateParams.weight=="less"){
                            getLessMore("不足",meat,0);
                        }else if($stateParams.weight=="just"){
                            getJust(meat,1);
                        }else{
                            getLessMore("多了",meat,2);
                        }
                    }
                }
                if($stateParams.type =="milletandpotato"){//谷类
                    $scope.info.title = "谷薯类";
                    $scope.info.img = "http://oss-cn-beijing.aliyuncs.com/xiaoerke-article-pic/yyglFoodCharacteristicMilletPotato";
                    if($stateParams.weight==""){//查看食物特点
                        getTrait();
                        $scope.foodTrait = milletandpotato[3].trait;

                    }else{//根据食物量多量少的危害
                        $scope.sug = true;
                        $scope.tra = true;
                        if($stateParams.weight=="less"){
                            getLessMore("不足",milletandpotato,0);
                        }else if($stateParams.weight=="just"){
                            getJust(milletandpotato,1);
                        }else{
                            getLessMore("多了",milletandpotato,2);
                        }
                    }
                }
                if($stateParams.type =="water"){//水类
                    $scope.info.title = "水类";
                    $scope.info.img = "http://oss-cn-beijing.aliyuncs.com/xiaoerke-article-pic/yyglFoodCharacteristicWater";
                    if($stateParams.weight==""){//查看食物特点
                        getTrait();
                        $scope.foodTrait = water[3].trait;

                    }else{//根据食物量多量少的危害
                        $scope.sug = true;
                        $scope.tra = true;
                        if($stateParams.weight=="less"){
                            getLessMore("不足",water,0);
                        }else if($stateParams.weight=="just"){
                            getJust(water,1);
                        }else{
                            getLessMore("多了",water,2);
                        }
                    }
                }
                if($stateParams.type =="vegetables"){//蔬菜类
                    $scope.info.title = "蔬菜类";
                    $scope.info.img = "http://oss-cn-beijing.aliyuncs.com/xiaoerke-article-pic/yyglFoodCharacteristicVegetables";
                    if($stateParams.weight==""){//查看食物特点
                        getTrait();
                        $scope.foodTrait = vegetables[3].trait;

                    }else{//根据食物量多量少的危害
                        $scope.sug = true;
                        $scope.tra = true;
                        if($stateParams.weight=="less"){
                            getLessMore("不足",vegetables,0);
                        }else if($stateParams.weight=="just"){
                            getJust(vegetables,1);
                        }else{
                            getLessMore("多了",vegetables,2);
                        }
                    }
                }
            }else{
                $scope.type = "false";
                $scope.info.img = "http://xiaoerke-healthplan-pic.oss-cn-beijing.aliyuncs.com/nutrition%2Findex_read1.png";
                GetEvaluate.get({"flag":"evaluate"}, function (data) {
                    if(data.todayEvaluateMap.fishEggs!=undefined){
                        if(data.todayEvaluateMap.fishEggs!=""){
                            if(data.todayEvaluateMap.fishEggs.split(";")[1]=="just"){//正好
                                getAll("鱼禽肉蛋类","正好",food,fishEggs,1,4);
                            }else if(data.todayEvaluateMap.fishEggs.split(";")[1]=="more"){//多了
                                getAll("鱼禽肉蛋类","多了",food,fishEggs,2,4);
                            }else{
                                getAll("鱼禽肉蛋类","不足",food,fishEggs,0,4);
                            }
                        }else{
                            getAll("鱼禽肉蛋类","不足",food,fishEggs,0,4);
                        }

                        if(data.todayEvaluateMap.meat!=""){
                            if(data.todayEvaluateMap.meat.split(";")[1]=="just"){//正好
                                getAll("水果类","正好",food1,meat,1,2);
                            }else if(data.todayEvaluateMap.meat.split(";")[1]=="more"){//多了
                                getAll("水果类","多了",food1,meat,2,2);
                            }else{
                                getAll("水果类","不足",food1,meat,0,2);
                            }
                        }else{
                            getAll("水果类","不足",food1,meat,0,2);
                        }

                        if(data.todayEvaluateMap.milk!=""){
                            if(data.todayEvaluateMap.milk.split(";")[1]=="just"){//正好
                                getAll("奶类","正好",food2,milk,1,5);
                            }else if(data.todayEvaluateMap.milk.split(";")[1]=="more"){//多了
                                getAll("奶类","多了",food2,milk,2,5);
                            }else{
                                getAll("奶类","不足",food2,milk,0,5);
                            }
                        }else{
                            getAll("奶类","不足",food2,milk,0,5);
                        }


                        if(data.todayEvaluateMap.oilSalt!=""){
                            if(data.todayEvaluateMap.oilSalt.split(";")[1]=="just"){//正好
                                getAll("油盐类","正好",food3,oilSalt,1,6);
                            }else if(data.todayEvaluateMap.oilSalt.split(";")[1]=="more"){//多了
                                getAll("油盐类","多了",food3,oilSalt,2,6);
                            }else{
                                getAll("油盐类","不足",food3,oilSalt,0,6);
                            }
                        }else{
                            getAll("油盐类","不足",food3,oilSalt,0,6);
                        }

                        if(data.todayEvaluateMap.potato!="") {
                            if (data.todayEvaluateMap.potato.split(";")[1] == "just") {//正好
                                getAll("谷薯类","正好",food4,milletandpotato,1,1);
                            } else if (data.todayEvaluateMap.potato.split(";")[1] == "more") {//多了
                                getAll("谷薯类","多了",food4,milletandpotato,2,1);
                            } else {
                                getAll("谷薯类","不足",food4,milletandpotato,0,1);
                            }
                        }else{
                            getAll("谷薯类","不足",food4,milletandpotato,0,1);
                        }

                        if(data.todayEvaluateMap.vegetables!="") {
                            if (data.todayEvaluateMap.vegetables.split(";")[1] == "just") {//正好
                                getAll("蔬菜类","正好",food5,vegetables,1,3);
                            } else if (data.todayEvaluateMap.vegetables.split(";")[1] == "more") {//多了
                                getAll("蔬菜类","多了",food5,vegetables,2,3);
                            } else {
                                getAll("蔬菜类","不足",food5,vegetables,0,3);
                            }
                        }else{
                            getAll("蔬菜类","不足",food5,vegetables,0,3);
                        }

                        if(data.todayEvaluateMap.water!=""){
                            if(data.todayEvaluateMap.water.split(";")[1]=="just"){//正好
                                getAll("水类","正好",food6,water,1,0);
                            }else if(data.todayEvaluateMap.water.split(";")[1]=="more"){//多了
                                getAll("水类","多了",food6,water,2,0);
                            }else{
                                getAll("水类","不足",food6,water,0,0);
                            }
                        }else{
                            getAll("水类","不足",food6,water,0,0);
                        }
                    }else{
                        getAll("水类","不足",food6,water,0,0);
                        getAll("谷薯类","不足",food4,milletandpotato,0,1);
                        getAll("水果类","不足",food1,meat,0,2);
                        getAll("蔬菜类","不足",food5,vegetables,0,3);
                        getAll("鱼禽肉蛋类","不足",food,fishEggs,0,4);
                        getAll("奶类","不足",food2,milk,0,5);
                        getAll("油盐类","不足",food3,oilSalt,0,6);
                    }

                });
            }

        });


        function getLessMore(type,type2,index){
            $scope.foodType = type;
            $scope.info.tad = "危害";
            $scope.foodTrait = type2[index].harm;
            $scope.info.tad2= "建议";
            $scope.foodSuggest = type2[index].suggest;
        }

        function getJust(type,index){
            $scope.tra = false;
            $scope.foodType = "正好";
            $scope.info.tad2= "建议";
            $scope.foodSuggest = type[index].suggest;
        }

        function getAll(title,sug,food,type,index,listindex){
            food.title=title;
            food.sug=sug;
            food.suggest=type[index].suggest;
            $scope.foodList[listindex] = food;
        }

        function getTrait(){
            $scope.tra = true;
            $scope.sug = false;
            $scope.info.tad = "营养特点";
            $scope.foodType = "营养特点";
        }


    }]);

