package com.incra.model

import java.util.Set;

/**
 * Created by jeff on 12/4/15.
 */
@Data
public class JobDescription {
    Integer id;
    String name;
    String command;
    Integer priority;
    Set<JobParameter> parameters;
}

