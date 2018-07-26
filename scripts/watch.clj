(require '[cljs.build.api :as b])

(b/watch "src"
  {:main 'homepage.core
   :output-to "out/homepage.js"
   :output-dir "out"})
