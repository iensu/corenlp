# corenlp

A Clojure library for playing around with Stanford's CoreNLP in Chinese.

I found it hard to find any kind of documentation for how to work with Chinese using the CoreNLP library. Hopefully this project will be of help to somebody else as well.

## Usage

First, you need to download the Chinese models from the bottom of [the Stanford CoreNLP page](http://nlp.stanford.edu/software/corenlp.shtml#Download) and unpack them under  `resources/models` in the project. Or you can put them anywhere you like, if you update the `config/chinese.properties` file to point to the models' locations.

Now you can try it out, either in a script or in a REPL.

```clojure
(require 'corenlp.parser)

; First you need to create a pipeline, this will take a while
(def pipeline (corenlp.parser/create-pipeline))

; Partially apply the pipeline to the parse function
(def parse (partial corenlp.parser/parse pipeline))

(def result (parse "你好，世界！"))

(println (str result))
```

The above should print:

```
{
Input text: 你好，世界！
Annotations:
你好，世界！

你 (PN O)
好 (VA O)
， (PU O)
世界 (NN O)
！ (PU O)

(ROOT
  (IP
    (NP (PN 你))
    (VP (VA 好))
    (PU ，)
    (NP (NN 世界))
    (PU ！)))
}
```

Another example

```clojure
(require 'corenlp.parser)

(defn noun? [token]
  (= "NN" (:pos token)))

(def parse (partial corenlp.parser/parse (corenlp.parser/create-pipeline)))

(def annotated-text (parse "你好，世界！"))

; Get a list of all nouns
(def nouns (flatten (map #(filter noun? (:tokens %))
                         (:sentences annotated-text))))
```

## License

Copyright © 2015 Jens Östlund

Distributed under the Eclipse Public License either version 1.0 or (at
your option) any later version.
