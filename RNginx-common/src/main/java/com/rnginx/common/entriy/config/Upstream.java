package com.rnginx.common.entriy.config;

import com.rnginx.common.entriy.LoadBalance;
import io.vertx.core.json.JsonObject;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

/**
 * @program: RNginx
 * @description: 上游结点
 * @author: 任鹏宇
 * @create: 2022-06-30 16:54
 **/

@Getter
@Setter
public class Upstream  extends BaseProxyConfig implements  IProxyConfig<Upstream>{


    /**
     * 默认不加权轮询
     */
    public static final String DEFAULT_TYPE = "robin";

    // 名称 唯一
    private String name;


    private String type;

    private LoadBalance lb;


    private List<String> nodes;


    @Override
    public void initConfig(JsonObject config) {
        this.name = config.getString(UPSTREAM_KEY_NAME);
        this.type = config.getString(UPSTREAM_KEY_TYPE,DEFAULT_TYPE);
        this.nodes = new ArrayList<>();
        config.getJsonArray(UPSTREAM_KEY_NODES).forEach(node->{
            nodes.add(String.valueOf(node));
        });

        this.lb = LoadBalance.nameOf(this.type);
    }

    @Override
    public Upstream getConfig() {
        return this;
    }

    @Override
    public Upstream getConfig(JsonObject config) {
        initConfig(config);
        return this;
    }
}
