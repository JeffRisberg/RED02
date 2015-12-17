package com.incra

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.scala.DefaultScalaModule
import com.incra.config.{RedisConfig, ArchaiusAppConfig}
import com.incra.model.{JobParameter, JobDescription}
import com.incra.service.RedisClient
import redis.clients.jedis.Jedis

import scala.collection.JavaConverters._
import scala.collection.immutable
import scala.collection.mutable.ListBuffer

/**
 * @author Jeff Risberg
 * @since 12/01/15
 */
object Main {
  def main(args: Array[String]): Unit = {

    val appConfig = new ArchaiusAppConfig()
    val redisConfig = new RedisConfig(appConfig, "redis")
    val redis = new RedisClient(redisConfig)

    redis.set("foo", "bar")
    val fooValue = redis.get("foo")
    println(fooValue)

    redis.zadd("sose", 0, "car")
    redis.zadd("sose", 0, "bike")
    val sose = redis.zrange("sose", 0, -1)

    val jobs = new ListBuffer[JobDescription]
    jobs.append(JobDescription(1, "alpha", "s3://xyz14", "java -cp", 10, Set.empty))
    jobs.append(JobDescription(2, "beta", "s3://def22", "java -cp", 5, Set(new JobParameter("x", 5))))
    jobs.append(JobDescription(3, "gamma", "s3://dwer77", "java -cp", 7, Set(new JobParameter("x", 5), new JobParameter("y", "Example"))))

    // Populate jobs
    val objectMapper = new ObjectMapper()
    objectMapper.registerModule(DefaultScalaModule)

    redis.del("jobs")

    jobs.foreach(job => {
      val jobJson = objectMapper.writeValueAsString(job)

      redis.set("job:" + job.id, jobJson)
      redis.lpush("jobs", jobJson)
    })

    // Dump all keys
    val keyList = redis.keys("*").asScala
    keyList.foreach { key => println("Stored key " + key) }

    // Dump all jobs
    val length = redis.llen("jobs")
    println("There are " + length + " jobs")

    val valuesSet = redis.lrange("jobs", 0, 150).asScala
    valuesSet.foreach(value => {
      println("Stored value " + value)

      val job = objectMapper.readValue(value, classOf[JobDescription])
      println(job)
    })
  }
}
