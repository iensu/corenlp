(ns corenlp.parser
  (:require [clojure.java.io :as io]
            [clojure.string :as string]
            [corenlp.records :refer [create-annotated-text]])
  (:import (edu.stanford.nlp.pipeline StanfordCoreNLP
                                      Annotation)))

(def default-props-fpath "Path to default properties file." "config/chinese.properties")

(defn- get-properties
  "Returns a Properties instance with data from a properties file"
  [props-fpath]
  (with-open [reader (io/reader props-fpath)]
    (doto (new java.util.Properties) (.load reader))))

(defn create-pipeline
  "Returns an initialized Stanford CoreNLP Annotation pipeline, uses Chinese settings by default"
  ([]
   (new StanfordCoreNLP (get-properties default-props-fpath)))
  ([props-fpath]
   (new StanfordCoreNLP (get-properties props-fpath))))

(defn parse
  "Takes an initialize Stanford CoreNLP pipeline and a string, and returns a lazy seq of AnnotatedText records."
  [pipeline text]
  (let [document (new Annotation text)]
    (do
      (. pipeline annotate document)
      (create-annotated-text document))))

(defn write-annotated-text-seq
  "Writes a seq of AnnotatedText records to a file, in UTF-8 encoding"
  [fpath annotated-text-seq]
  (spit fpath
        (string/join "\n\n" (map str annotated-text-seq))
        :encoding "UTF-8"))
