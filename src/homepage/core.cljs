(ns homepage.core
  (:require [reagent.core :as r]))

(enable-console-print!)

(def person {:name "Milan Blažek"
             :education #{"SPŠ a VOŠ Chomutov" "ČVUT Praha"}
             :titles #{"Ing." "Bc."}
             :skills '("Clojure" "Javascript" "Python" "C/C++" "Java" "PHP")
             :github "http://github.com/skallet"
             :email "blazekm1l4n@gmail.com"})

(defn render-syntax [text type]
  [:span {:class (str "syntax-" (name type))}
   text])

(defn render-line-number [n]
  [:span {:class "line-number"}
   [render-syntax (str n) :line-number]])

(defn tab [child]
  [:span.tab-left
   child])

(defn with-keys [l]
  (map-indexed #(with-meta %2  {:key %1}) l))

(defn href? [l]
  (>= (.indexOf (str l) "http") 0))

(defn render-value-type [v]
  (cond
    (href? v) [:span
               [render-syntax \" :string]
               [:a {:href (str v) :target "_blank"} (str v)]
               [render-syntax \" :string]]
    (string? v) [render-syntax (str \" v \") :string]
    (set? v) [:span
              [render-syntax "#{" :base]
              (with-keys (interpose [:span " "] (map render-value-type v)))
              [render-syntax "}" :base]]
    (list? v) [:span
               [render-syntax "'(" :base]
               (with-keys (interpose [:span " "] (map render-value-type v)))
               [render-syntax ")" :base]]
    :else [render-syntax v :string]))

(defn render-value [[k v]]
  [:span
   [render-syntax (str ":" (name k) " ") :kw]
   [render-value-type v]])

(defn render-last-line [c]
  [:span
   c
   [render-syntax (str "})") :base]])

(defn render-object [o next-line]
  (let [keys (keys o)
        nums (range next-line (+ next-line (count keys)))
        last-line (+ 1 (last nums))
        line-numbers (map render-line-number nums)
        line-values (map render-value o)

        last-value (render-last-line (last line-values))
        repeat-el (fn [e] (take (count keys) (repeat e)))
        whole-lines (interleave
                      line-numbers
                      (repeat-el [tab])
                      (concat (butlast line-values) (list last-value))
                      (repeat-el [:br]))
        lines (map-indexed #(with-meta %2  {:key %1}) whole-lines)]
    [:div
     lines
     [render-line-number last-line]
     [:br]
     [render-line-number (+ 1 last-line)]
     [:br]]))

(defn page []
  [:div
   [render-line-number 1]
   [render-syntax "(" :base]
   [render-syntax "ns " :fn]
   [render-syntax "milanblazek.cz" :base]
   [render-syntax ")" :base]
   [:br]
   [render-line-number 2]

   [:br]
   [render-line-number 3]
   [render-syntax "(" :base]
   [render-syntax "def " :fn]
   [render-syntax "me {" :base]
   [:br]
   [render-object person 4]])

(defn app []
  (r/render [#'page] (.getElementById js/document "app")))

(app)
