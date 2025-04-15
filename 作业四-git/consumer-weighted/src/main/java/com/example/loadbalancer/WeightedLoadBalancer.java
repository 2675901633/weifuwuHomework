package com.example.loadbalancer;

import org.springframework.beans.factory.ObjectProvider;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.DefaultResponse;
import org.springframework.cloud.client.loadbalancer.EmptyResponse;
import org.springframework.cloud.client.loadbalancer.Request;
import org.springframework.cloud.client.loadbalancer.Response;
import org.springframework.cloud.loadbalancer.core.ReactorServiceInstanceLoadBalancer;
import org.springframework.cloud.loadbalancer.core.ServiceInstanceListSupplier;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Random;

public class WeightedLoadBalancer implements ReactorServiceInstanceLoadBalancer {

    private final String serviceId;
    private final ObjectProvider<ServiceInstanceListSupplier> serviceInstanceListSupplierProvider;
    private final Random random = new Random();

    public WeightedLoadBalancer(ObjectProvider<ServiceInstanceListSupplier> serviceInstanceListSupplierProvider,
            String serviceId) {
        this.serviceId = serviceId;
        this.serviceInstanceListSupplierProvider = serviceInstanceListSupplierProvider;
    }

    @Override
    public Mono<Response<ServiceInstance>> choose(Request request) {
        ServiceInstanceListSupplier supplier = serviceInstanceListSupplierProvider.getIfAvailable();
        return supplier.get(request).next().map(serviceInstances -> processInstanceResponse(serviceInstances));
    }

    private Response<ServiceInstance> processInstanceResponse(List<ServiceInstance> instances) {
        if (instances.isEmpty()) {
            return new EmptyResponse();
        }

        // 权重分布：服务1(10%)，服务2(30%)，服务3(60%)
        int randomValue = random.nextInt(100);
        int port;

        if (randomValue < 10) {
            port = 8081; // 服务1的端口
        } else if (randomValue < 40) {
            port = 8082; // 服务2的端口
        } else {
            port = 8083; // 服务3的端口
        }

        // 找到对应端口的实例
        for (ServiceInstance instance : instances) {
            if (instance.getPort() == port) {
                return new DefaultResponse(instance);
            }
        }

        // 如果没找到，返回第一个实例
        return new DefaultResponse(instances.get(0));
    }
}