package com.mongodb.aws.demo.services;

import java.util.Objects;

import org.springframework.data.mongodb.core.MongoOperations;
import static org.springframework.data.mongodb.core.FindAndModifyOptions.options;
import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import com.mongodb.aws.demo.entity.DatabaseSequence;

@Service
public class SequenceGeneratorService {

	private MongoOperations mongoOperations;
	
	// @Autowired
	    public SequenceGeneratorService(MongoOperations mongoOperations) {
	        this.mongoOperations = mongoOperations;
	    }
	 
	public long generateSequence(String seqName) {
	    
		DatabaseSequence counter = mongoOperations.findAndModify(query(where("_id").is(seqName)),
	      new Update().inc("seq",1),options().returnNew(true).upsert(true),DatabaseSequence.class);
	    
		
		return !Objects.isNull(counter) ? counter.getSeq() : 1001;
	    
	}
}
