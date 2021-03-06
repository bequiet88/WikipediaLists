/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package de.unimannheim.dws.wikilist.test;

import com.hp.hpl.jena.query.Query ;
import com.hp.hpl.jena.query.QueryExecution ;
import com.hp.hpl.jena.query.QueryExecutionFactory ;
import com.hp.hpl.jena.query.QueryFactory ;
import com.hp.hpl.jena.query.ResultSet ;
import com.hp.hpl.jena.query.ResultSetFormatter ;
import com.hp.hpl.jena.rdf.model.ModelFactory ;

public class ExampleDBpedia2
{
    static public void main(String... argv) {
        String queryString =
        		"SELECT * WHERE {"
        		        +"?e <http://dbpedia.org/ontology/series> <http://dbpedia.org/resource/The_Sopranos>  ."
        		        +"?e <http://dbpedia.org/ontology/releaseDate> ?date ."
        		        +"?e <http://dbpedia.org/ontology/episodeNumber> ?number ."
        		    	+"?e <http://dbpedia.org/ontology/seasonNumber> ?season ."
        		    	+"}" + "ORDER BY DESC(?date)";
        		
        		
        		
//            "SELECT * WHERE { " +
//            "    SERVICE <http://dbpedia-live.openlinksw.com/sparql?timeout=2000> { " +
//            "        SELECT DISTINCT ?company where {?company a <http://dbpedia.org/ontology/Company>} LIMIT 20" +
//            "    }" +
//            "}" ;
        Query query = QueryFactory.create(queryString) ;
        QueryExecution qexec = QueryExecutionFactory.create(query, ModelFactory.createDefaultModel()) ;
        try {
            ResultSet rs = qexec.execSelect() ;
            ResultSetFormatter.out(System.out, rs, query) ;
        } finally {
            qexec.close() ;
        }
    }

}
