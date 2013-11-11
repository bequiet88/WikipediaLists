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

import java.util.ArrayList;

import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.QueryFactory;
import com.hp.hpl.jena.query.QuerySolution;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.query.ResultSetFormatter;
import com.hp.hpl.jena.sparql.engine.http.QueryEngineHTTP;

public class ExampleDBpedia1
{
    static public void main(String...argv)
    {
        try {
        	
        	String prefix = "PREFIX owl: <http://www.w3.org/2002/07/owl#>"
        			+"PREFIX xsd: <http://www.w3.org/2001/XMLSchema#>"
        			+"PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>"
        			+"PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>"
        			+"PREFIX foaf: <http://xmlns.com/foaf/0.1/>"
        			+"PREFIX dc: <http://purl.org/dc/elements/1.1/>"
        			+"PREFIX : <http://dbpedia.org/resource/>"
        			+"PREFIX dbpprop: <http://dbpedia.org/property/>"
        			+"PREFIX dbpedia: <http://dbpedia.org/>"
        			+"PREFIX skos: <http://www.w3.org/2004/02/skos/core#>"
        			+"PREFIX dbpedia-owl: <http://dbpedia.org/ontology/>"
        			+ "PREFIX dbpprop-de: <http://de.dbpedia.org/property/>";
        	
        	
            String queryStr = prefix + 
            		
            		
           /*
            * Query for http://en.wikipedia.org/wiki/List_of_Peers_1330-1339 		
            */
//            		"SELECT * WHERE {"
//                    //+"<http://dbpedia.org/resource/John_de_Warenne,_7th_Earl_of_Surrey> foaf:name ?name ."
//            		//                 +"<http://dbpedia.org/resource/James_Baldwin> dbpprop:dateOfBirth ?dob. }";
//                    +"<http://dbpedia.org/resource/Baron_Deincourt> dbpprop:title ?title. }";
//            		//+"<http://dbpedia.org/resource/Matthew_McCauley_(politician)> dbpprop:name $date. }";
            		
            		"SELECT *"
            		+"WHERE {"
            		    +"<http://de.dbpedia.org/resource/Acino_Holding> dbpprop-de:mitarbeiterzahl $val . }";
            		
            /*
             * Example 1
             */
//					"SELECT * WHERE {"
//					+"?e <http://dbpedia.org/ontology/episodeNumber> ?number ."
//                	+"?e <http://dbpedia	.org/ontology/seasonNumber> ?season ."
//                	+"}" + "ORDER BY DESC(?date)";
            /*
             * Example 2		
             */
//					"select distinct ?Concept where {[] a ?Concept} LIMIT 10";
            /*
             * Example 3
             */         
//            "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>"
//            	+"PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>"
//            	+"PREFIX foaf: <http://xmlns.com/foaf/0.1/>"
//            	+"PREFIX dbpedia-owl: <http://dbpedia.org/ontology>"
//            	+"PREFIX dbpprop: <http://dbpedia.org/property> "
//            	 
//            	+"SELECT DISTINCT ?film_title ?star_name "
//            	+"where {?film_title rdf:type <http://dbpedia.org/ontology/Film> . "
//            	+"?film_title  foaf:name ?film_name . "
//            	+"?film_title rdfs:comment ?film_abstract . "
//            	+"?film_title dbpedia-owl:starring ?star . "
//            	+"?star dbpprop:name ?star_name "
//            	+"}"+ "LIMIT 5";
            /*
             * Example 4
             */
//            		"SELECT * WHERE {"
//                +"?e <http://dbpedia.org/ontology/series> <http://dbpedia.org/resource/The_Sopranos>  ."
//                +"?e <http://dbpedia.org/ontology/releaseDate> ?date ."
//                +"?e <http://dbpedia.org/ontology/episodeNumber> ?number ."
//                +"?e <http://dbpedia.org/ontology/seasonNumber> ?season ."
//            	+"}" + "ORDER BY DESC(?date)";            		
//            
            
            Query query = QueryFactory.create(queryStr);

            // Remote execution.
            QueryExecution qexec = QueryExecutionFactory.sparqlService("http://de.dbpedia.org/sparql", query);
            // Set the DBpedia specific timeout.
            ((QueryEngineHTTP)qexec).addParam("timeout", "10000") ;

            // Execute.
            ResultSet rs = qexec.execSelect();
            //ResultSetFormatter.out(System.out, rs, query);
            
//            ArrayList<String> result = new ArrayList<String>();
//            
//            while(rs.hasNext()) {
//            	
//            	QuerySolution res = rs.next();
//            	result.add(res.toString());
//            }
            
            
            System.out.println(ResultSetFormatter.asXMLString(rs));//asText(rs));//toList(rs).toString());
            
            qexec.close();
        } catch (Exception e) {
            e.printStackTrace();
        }


    }
}
