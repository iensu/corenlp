(ns corenlp.perf-check
  (:require [corenlp.parser :refer [create-pipeline
                                    parse]]
            [clojure.java.io :as io]
            [clojure.string :as string]))

(defrecord TestResult [input length time time-unit])

(defn- clean-time
  [time-s]
  (let [time-s (string/trim (string/replace time-s #"\"" ""))
        ts (drop 2 (string/split time-s #" "))]
    (vector (read-string (first ts)) (last ts))))

(defn test-perf 
  [parse-fn input]
  (let [num-chars (count input)
        [t u] (clean-time (with-out-str (time (parse-fn input))))]
    (TestResult. input num-chars t u)))

(defn run-tests
  []
  (let [inputs (with-open [rdr (io/reader "/home/jens/host/sentences.txt")]
                 (doall (line-seq rdr)))
        parse (partial parse (create-pipeline))
        test-perf (partial test-perf parse)]
    (doall (map test-perf inputs))))

(defn time-setup []
  (clean-time (with-out-str (time (create-pipeline)))))
