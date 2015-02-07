(defproject corenlp "0.1.0-SNAPSHOT"
  :description "Playing around with the Stanford CoreNLP in Chinese"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.6.0"]
                 [edu.stanford.nlp/stanford-corenlp "3.5.0"]]
  :jvm-opts ["-Xmx2g"])
