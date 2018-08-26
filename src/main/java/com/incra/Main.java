package com.incra;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.slg.config.ArchaiusAppConfig;
import com.slg.config.RedisConfig;
import com.slg.model.JobDescription;
import com.slg.model.JobParameter;
import com.slg.service.RedisClient;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 * @author Jeff Risberg
 * @since 11/29/15
 */
public class Main {

    public static void main(String[] args) throws Exception {

        ArchaiusAppConfig appConfig = new ArchaiusAppConfig();
        RedisConfig redisConfig = new RedisConfig(appConfig, "redis");
        RedisClient redis = new RedisClient(redisConfig);

        redis.set("foo", "bar");
        String fooValue = redis.get("foo");
        System.out.println(fooValue);

        redis.zadd("sose", 0, "car");
        redis.zadd("sose", 0, "bike");
        Set<String> sose = redis.zrange("sose", 0, -1);

        List<JobDescription> jobs = new ArrayList<JobDescription>();

        Set params1 = new HashSet();
        jobs.add(new JobDescription(1, "alpha", "s3://xyz14", "java -cp", 10, params1));

        Set params2 = new HashSet();
        params2.add(new JobParameter("x", 5));
        jobs.add(new JobDescription(2, "beta", "s3://def22", "java -cp", 5, params2));

        Set params3 = new HashSet();
        params3.add(new JobParameter("x", 5));
        params3.add(new JobParameter("y", "Example"));
        jobs.add(new JobDescription(3, "gamma", "s3://dwer77", "java -cp", 7, params3));

        // Populate jobs
        ObjectMapper objectMapper = new ObjectMapper();

        redis.del("jobs");

        for (JobDescription job : jobs) {
            String jobJson = objectMapper.writeValueAsString(job);

            redis.set("job:" + job.getId(), jobJson);
            redis.lpush("jobs", jobJson);
        }

        // Dump all keys
        Set<String> keyList = redis.keys("*");
        Iterator iter1 = keyList.iterator();
        while (iter1.hasNext()) {
            Object key = iter1.next();
            System.out.println("Stored key " + key);
        }

        // Dump all jobs
        Long length = redis.llen("jobs");
        System.out.println("There are " + length + " jobs");

        List<String> values = redis.lrange("jobs", 0, 150);
        Iterator iter2 = values.iterator();
        while (iter2.hasNext()) {
            String value = (String) iter2.next();
            System.out.println("Stored value " + value);

            JobDescription job = objectMapper.readValue(value, JobDescription.class);
            System.out.println(job);
        }
    }
}
