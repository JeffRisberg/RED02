package com.incra.model

/**
 * Created by jeff on 12/4/15.
 */
case class JobDescription
(
  id: Integer,
  name: String,
  url: String,
  command: String,
  priority: Integer,
  parameters: Set[JobParameter]
  )
