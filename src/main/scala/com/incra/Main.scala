package com.incra

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.scala.DefaultScalaModule
import com.incra.model.JobDescription
import redis.clients.jedis.Jedis

import scala.collection.JavaConverters._
import scala.collection.mutable.ListBuffer

/**
 * @author Jeff Risberg
 * @since 12/01/15
 */
object Main {
  def main(args: Array[String]): Unit = {

    val jedis = new Jedis("52.27.1.250", 6379)
    //jedis.auth("password");
    System.out.println("Connected to Redis")

    jedis.set("foo", "bar")
    val fooValue = jedis.get("foo")
    println(fooValue)

    jedis.zadd("sose", 0, "car")
    jedis.zadd("sose", 0, "bike")
    val sose = jedis.zrange("sose", 0, -1)

    val jobs = new ListBuffer[JobDescription]
    jobs.append(JobDescription(1, "alpha", "s3://xyz14", "java -cp", 10))
    jobs.append(JobDescription(2, "beta", "s3://def22", "java -cp", 5))
    jobs.append(JobDescription(3, "gamma", "s3://dwer77", "java -cp", 7))

    // Populate jobs
    val objectMapper = new ObjectMapper()
    objectMapper.registerModule(DefaultScalaModule)

    jedis.del("jobs")

    jobs.foreach(job => {
      val jobJson = objectMapper.writeValueAsString(job)

      jedis.set("job:" + job.id, jobJson)
      jedis.lpush("jobs", jobJson)
    })

    // Dump all keys
    val keyList = jedis.keys("*").asScala
    keyList.foreach { key => println("Stored key " + key) }

    // Dump all jobs
    val length = jedis.llen("jobs")
    println("There are " + length + " jobs")

    val valuesSet = jedis.lrange("jobs", 0, 150).asScala
    valuesSet.foreach(value => {
      println("Stored value " + value)

      val job = objectMapper.readValue(value, classOf[JobDescription])
      println(job)
    })
  }
}
