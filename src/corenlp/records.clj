(ns corenlp.records
  (:require [clojure.string :as string])
  (:import (edu.stanford.nlp.ling CoreAnnotations$SentencesAnnotation
                                  CoreAnnotations$TokensAnnotation
                                  CoreAnnotations$TextAnnotation
                                  CoreAnnotations$PartOfSpeechAnnotation
                                  CoreAnnotations$NamedEntityTagAnnotation)
           (edu.stanford.nlp.trees TreeCoreAnnotations$TreeAnnotation)))

(defrecord Token [text pos ne]
  Object
  (toString [{:keys [text pos ne]}]
    (format "%s (%s %s)" text pos ne)))

(defrecord Sentence [text tokens tree]
  Object
  (toString [{:keys [text tokens tree]}]
    (let [tokens (map str tokens)] 
      (string/join "\n\n" [text (string/join "\n" tokens) (.pennString tree)]))))

(defrecord AnnotatedText [text sentences]
  Object
  (toString [{:keys [text sentences]}]
    (let [sentences (map str sentences)]
      (format "{\nInput text: %s\nAnnotations:\n%s}" text (string/join "\n" sentences)))))

(defn- create-token
  "Takes a TokenAnnotation t and returns a populated Token record"
  [t]
  (Token. (. t get CoreAnnotations$TextAnnotation)
          (. t get CoreAnnotations$PartOfSpeechAnnotation)
          (. t get CoreAnnotations$NamedEntityTagAnnotation)))

(defn- create-sentence
  "Takes a sentence annotation and returns a populated Sentence record"
  [s]
  (Sentence. (. s get CoreAnnotations$TextAnnotation)
             (map create-token (. s get CoreAnnotations$TokensAnnotation))
             (. s get TreeCoreAnnotations$TreeAnnotation)))

(defn create-annotated-text
  "Takes an Annotation document and returns an AnnotatedText record containing the text and its Sentence records."
  [d]
  (AnnotatedText. (. d get CoreAnnotations$TextAnnotation)
                  (map create-sentence (. d get CoreAnnotations$SentencesAnnotation))))
