angular.module('controllers', ['ionic']).controller('cMotherMistakesCtrl', [
    '$scope','$http',
    function ($scope,$http) {

        $scope.num = [11,22,33,44,55];

        var pData = {logContent:encodeURI("BMGL_8")};
        $http({method:'post',url:'ap/util/recordLogs',params:pData});

        $scope.imgOpen = "http://xiaoerke-common-pic.oss-cn-beijing.aliyuncs.com/common%2Farr_blue_down.png";
        $scope.mistakeList=[
            {
                img:"http://xiaoerke-healthplan-pic.oss-cn-beijing.aliyuncs.com/constipation%2Fmother_mistakes1.png",
                title:"宝宝便秘就喝蜂蜜",
                text:[
                    "蜂蜜在酿造、运输与储存的过程中，常受到肉毒杆菌的污染，这是因为，蜜蜂在采取花粉酿蜜的过程中，" +
                    "很有可能会把被肉毒杆菌污染的花粉和蜜带回蜂箱，所以蜂蜜中含有的肉毒杆菌芽孢非常高。" +
                    "而肉毒杆菌的芽胞适应能力很强，它在100℃的高温下仍然可以存活。婴儿的抗病能力差，" +
                    "非常容易使入口的肉毒杆菌在肠道中繁殖，并产生毒素，而婴儿肝脏的解毒功能又差，因而引起肉毒杆菌性食物中毒。 ",
                    "当然并不是所有婴儿一旦接触到了肉毒杆菌芽孢，都有可能引起肉毒杆菌中毒。" +
                    "但是吃蜂蜜的宝宝会比不吃蜂蜜的宝宝患病率高出8倍，因此在宝宝2岁之前，请勿添加蜂蜜，" +
                    "和蜂蜜比起来，其他的糖类补充更加安全，可以用新鲜的果汁来进行取代蜂蜜给宝宝补充营养。"
                ],

                moreText:"查看更多"

            },
            {
                img:"http://xiaoerke-healthplan-pic.oss-cn-beijing.aliyuncs.com/constipation%2Fmother_mistakes2.png",
                title:"香蕉不一定治便秘",
                text:[
                    "大家都知道，香蕉未成熟时，外皮呈青绿色，剥去外皮，涩得不能下咽。熟透了的香蕉，涩味一扫而净" +
                    "，软糯香甜，深受孩子和老年人的喜爱。香蕉是热带、亚热带的水果，为了便于保存和运输，采摘香蕉的时候，" +
                    "不能等它熟了，而是在香蕉皮青绿时就得摘下入库。我们在北方吃到的香蕉都是经过催熟后才成熟的。" +
                    "生香蕉的涩味来自于香蕉中含有的大量的鞣酸。当香蕉成熟之后，虽然已尝不出涩味了，但鞣酸的成分仍然存在。" +
                    "鞣酸具有非常强的收敛作用，可以将粪便结成干硬的粪便，从而造成便秘。最典型的是老人、孩子吃过香蕉之后，" +
                    "非但不能帮助通便，反而可发生明显的便秘。"

                ],
                moreText:"查看更多"

            },
            {
                img:"http://xiaoerke-healthplan-pic.oss-cn-beijing.aliyuncs.com/constipation%2Fmother_mistakes3.png",
                title:"宝宝便秘就喝金银花露",
                text:[
                    " 金银花露味道甘甜，价格不贵，宝宝通便效果又好，很多家长采取长期让宝宝饮用金银花露来治疗便秘" +
                    "，特别是到了秋季，天干物燥，家长们认为孩子内火重，更需要喝金银花露，经常用它代替宝宝的日常饮水" +
                    "，但实际上，这种方法是不可取的。",
                    "全国著名的消化内科王萍主任医师介绍：引起婴儿便秘的原因有很多，饮食成分不当、肠道功能异常、消化道发育畸形等都可以引起便秘。" +
                    "所以宝宝便秘不可盲目服用金银花露，一定要查明原因，或调节饮食，或对症用药。由于金银花露性质偏凉，" +
                    "长期饮用对宝宝幼嫩的脾胃有一定的伤害，影响正常的消化系统功能，还会形成依赖性，所以宝宝便秘千万不要长期饮用金银花露。"
                ],
                moreText:"查看更多"

            },
            {
                img:"http://xiaoerke-healthplan-pic.oss-cn-beijing.aliyuncs.com/constipation%2Fmother_mistakes4.png",
                title:"宝宝便秘可以通过喝凉茶来治疗",
                text:[
                    "广东省妇幼保健院的儿科主任医师王淑珍说：这种观点是不正确，喝降火茶对治疗便秘是不顶用的。" +
                    "从宝宝发生便秘的原因来看，便秘并不是说完全像我们平时所说的“上火了”，" +
                    "更关键的原因是从宝宝的发育情况来看，宝宝的胃肠道调节功能不完善，调节功能差，蠕动力也差，" +
                    "这才是导致便秘的症结所在。而盲目地使用降火茶的并不能改善，甚至可以说是“头痛医脚”，延误病情。" +
                    "要治疗便秘，关键还是要刺激肠蠕动，这样才是对症的。" ,
                    "另外，王淑珍主任还特别强调一点，在广东尤其是广州，因为地理气候的关系，人们有一种传统的养生之法——喝凉茶。" +
                    "其实，对于宝宝来说，过多地喝凉茶也是没好处，甚至会出现一些反作用。首先，它会影响宝宝的胃口，" +
                    "不利于吸取充分的食物营养；其次，市面上的一些凉茶成分并不是特别纯，有的甚至影响宝宝的生长发育。" +
                    "因此，建议家长不要养成经常给宝宝喝凉茶降火的习惯。"
                ],
                moreText:"查看更多"
            },
            {
                img:"http://xiaoerke-healthplan-pic.oss-cn-beijing.aliyuncs.com/constipation%2Fmother_mistakes5.png",
                title:"宝宝便秘就是因为上火了",
                text:[
                    "宝宝出现便秘，许多妈妈都以为是太热气了，上火了，其实便秘可能由许多原因引起。" +
                    "饮食不足、食物成分结构不良、肠道功能失常、疾病因素、精神因素等都可能引起宝宝出现便秘现象。" +
                    "宝宝上火通常会多方面反应出来：发热、口腔溃疡/糜烂、厌食、便秘，还有眼红、眼屎多、嘴唇干裂、嗓子干涩、" +
                    "口臭、腹胀、腹痛，因此小儿烦躁易怒、易哭。很多妈妈一看宝宝便秘就认为上火了，然后给宝宝吃清火的一些中药产品。" +
                    "虽然这类产品可以暂时缓解宝宝便秘问题。但实际对宝宝伤害很大的，宝宝的肠胃、肝、脾等都很娇嫩，" +
                    "市场上一些清火产品一般都采用了金银花、菊花等都属性寒的食物,对宝宝娇嫩的胃肠刺激很大," +
                    "而且凉了就会加速肠蠕动,甚至引起腹泻。而另有一些清火产品则添加了中药成份，那对宝宝的肝脾的伤害则更大，" +
                    "毕竟是药三分毒。"
                ],
                moreText:"查看更多"

            },
            {
                img:"http://xiaoerke-healthplan-pic.oss-cn-beijing.aliyuncs.com/constipation%2Fmother_mistakes6.png",
                title:"吃苹果治便秘？生吃熟吃效果不一样",
                text:[
                    "苹果中含有比香蕉还多的膳食纤维，以刺激肠道使有助排便，未经加热的生苹果中的果胶有软化大便缓解便秘的作用。" ,
                    "不过，有些妈妈觉得苹果直接吃一来生冷，二来质地太硬，于是就会给宝宝蒸熟来吃，殊不知苹果蒸熟之后" +
                    "，原本缓解便秘的果胶却摇身一变，变成具有收敛、止泻功效的成分，再加上苹果中的鞣酸是肠道收敛剂，" +
                    "它能使大便内水分减少，有止泻的作用。因此，吃了煮熟的苹果，不仅没有缓解便秘的作用，反而可能加重便秘。"
                ],
                moreText:"查看更多"

            },
            {
                img:"http://xiaoerke-healthplan-pic.oss-cn-beijing.aliyuncs.com/constipation%2Fmother_mistakes7.png",
                title:"酸奶能治疗便秘？",
                text:[
                    "网上流传喝酸奶能治疗便秘，因为酸奶里面含有益生菌，能增加肠胃蠕动，从而促进排便。" +
                    "酸奶中的双歧杆菌确实能对人体肠道调节有益，但是酸奶里面的益生菌含量很少，一般为2%-5%，" +
                    "因此根本达不到治疗便秘的效果，平时喝酸奶最多有帮助消化的效果。",
                    "此外，酸奶为冷冻食品，一般从冰箱里拿出来之后要放至常温，才能给宝宝食用，否则宝宝幼小的肠胃受不了。" +
                    "1岁以前的宝宝不能喝酸奶，3岁以前的宝宝喝酸奶也要适量。"
                ],
                moreText:"查看更多"
            }
        ]

        $scope.lookMore=function(index){
            $scope.index=index;
            if( $scope.mistakeList[index].moreText=="查看更多"){
                $scope.mistakeList[index].moreText="收起";;
                $(".more").eq($scope.index).siblings("dd").removeClass("select");
                $(".more").eq($scope.index).children("img").attr("src","http://xiaoerke-common-pic.oss-cn-beijing.aliyuncs.com/common%2Farr_blue_up.png")

            }
            else{
                $scope.mistakeList[index].moreText="查看更多";;
                $(".more").eq($scope.index).siblings("dd").addClass("select");
                $(".more").eq($scope.index).children("img").attr("src","http://xiaoerke-common-pic.oss-cn-beijing.aliyuncs.com/common%2Farr_blue_down.png")
            }
        }
    }]);

