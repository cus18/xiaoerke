package com.cxqm.xiaoerke.modules.search.service.impl;

import java.util.HashMap;
import java.util.Map;

import com.cxqm.xiaoerke.common.config.Global;
import com.opensearch.javasdk.CloudsearchClient;
import com.opensearch.javasdk.CloudsearchSearch;
import com.opensearch.javasdk.object.KeyTypeEnum;

public class XiaoerkeOpenSearch {
	private static XiaoerkeOpenSearch instance = new XiaoerkeOpenSearch();
	private CloudsearchSearch search = null;

	private XiaoerkeOpenSearch(){
		init();
	}

	static XiaoerkeOpenSearch getInstance(){
		return instance;
	}

	private void init(){
		String accesskey = Global.getConfig("aliyun.accesskey");
		String secret =  Global.getConfig("aliyun.secret");

		Map<String, Object> opts = new HashMap<String, Object>();

		// 这里的host需要根据访问应用详情页中提供的的API入口来确定
		opts.put("host", Global.getConfig("opensearch.host"));
		CloudsearchClient client = new CloudsearchClient(accesskey, secret , opts, KeyTypeEnum.ALIYUN);

		search = new CloudsearchSearch(client);
		// 添加指定搜索的应用：
		search.addIndex(Global.getConfig("opensearch.illness"));
		search.addIndex(Global.getConfig("opensearch.doctor"));
		search.addIndex(Global.getConfig("opensearch.hospital"));

		search.setFormat("json");
	}

	CloudsearchSearch getCloudsearch(){
		return search;
	}

}

