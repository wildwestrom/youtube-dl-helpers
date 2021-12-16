#!/usr/bin/env bb
(ns script
  (:require [babashka.process :as p]))

(assert (= 1 (count *command-line-args*)) "Please give one link to a YouTube video as an argument.")

;; Not sure I actually need this
(assert (= java.lang.String (type (first *command-line-args*))))

(def link (first *command-line-args*))

@(p/process ["youtube-dl" "-F" link]
            {:inherit true
             :shutdown p/destroy-tree})

(defn try-download []
  (println "\nSelect a format (or a video+audio format), by number:")
  (let [errcode (:exit @(p/process ["youtube-dl" "-f" (read-line) link]
                                   {:inherit true}))]
    ;; Check if successful. If not, try again.
    (when-not (= 0 errcode)
      (try-download))))

(try-download)
