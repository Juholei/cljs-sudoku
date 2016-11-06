(ns sudoku.lib-sudoku
  (:require [clojure.set :as set]))

(def all-values #{1 2 3 4 5 6 7 8 9})

(defn value-at [board coord]
  (get-in board coord))

(defn set-value-at [board coord new-value]
  (assoc-in board coord new-value))

(defn- row-values [board coord]
  (let [[row _] coord]
    (get-in board [row])))

(defn- col-values [board coord]
  (let [[_ col] coord
        column-values (fn [acc row]
                        (conj acc (get-in row [col])))]
    (reduce column-values '() board)))

(defn- block-top-left [coords]
  (let [[x y] coords]
    [(* 3 (int (/ x 3))) (* 3 (int (/ y 3)))]))

(defn- block-values [board coord]
  (let [[x y] (block-top-left coord)
        coords (for [offset-x [0 1 2]
                     offset-y [0 1 2]]
                 [(+ x offset-x) (+ y offset-y)])
        values (fn [acc xy]
                 (conj acc (value-at board xy)))]
    (reduce values '() coords)))

(defn duplicate? [board coord value]
  (not (or (> (get (frequencies (filter pos? (row-values board coord))) value) 1)
           (> (get (frequencies (filter pos? (col-values board coord))) value) 1)
           (> (get (frequencies (filter pos? (block-values board coord))) value) 1))))

(defn- rows [board]
  (for [row (range 0 9)]
    (row-values board [row 0])))

(defn- cols [board]
  (for [col (range 0 9)]
    (col-values board [0 col])))

(defn- blocks [board]
  (for [row [0 3 6]
        col [0 3 6]]
    (block-values board [row col])))

(defn- valid-rows? [board]
  (every? true? (for [row (rows board)]
                  (= (set row) all-values))))

(defn- valid-cols? [board]
  (every? true? (for [col (cols board)]
                  (= (set col) all-values))))

(defn- valid-blocks? [board]
  (every? true? (for [block (blocks board)]
                  (= (set block) all-values))))

(defn valid-solution? [board]
  (and (valid-rows? board)
       (valid-cols? board)
       (valid-blocks? board)))

(defn has-value? [board coord]
  ((complement zero?) (value-at board coord)))

(defn valid-values-for [board coord]
  (if (has-value? board coord)
    #{}
    (let [values (set/union (block-values board coord)
                            (row-values board coord)
                            (col-values board coord))]
      (set/difference all-values values))))

(defn filled? [board]
  (let [board-set (set (apply concat board))]
    ((complement contains?) board-set 0)))

(defn find-empty-point [board]
  (let [coords (for [row (range 0 9)
                     col (range 0 9)]
                 [row col])
        zero-value? (fn [coords]
          (cond
            (empty? coords) nil
            (zero? (value-at board (first coords))) (first coords)
            :else (recur (rest coords))))]
    (zero-value? coords)))

(defn solve-helper [board]
  (if (filled? board)
    (when (valid-solution? board)
      [board])
      (let [empty-spot (find-empty-point board)]
        (for [valid-value (valid-values-for board empty-spot)
              new-board (solve-helper (set-value-at board empty-spot valid-value))]
            (do (println "solving")
            (when (seq new-board)
              new-board))))))

(defn solve [board]
  (first (solve-helper board)))

(defn generate-board []
  (let [random-value-to-board (fn [board value]
                                 (let [row (rand-int 9)
                                       column (rand-int 9)]
                                   (if (and (duplicate? (set-value-at board [row column] value) [row column] value)
                                            (not (has-value? board [row column])))
                                     (set-value-at board [row column] value)
                                     board)))
        empty-board (vec (repeat 9 (vec (repeat 9 0))))
        values (repeatedly 45 #(inc (rand-int 9)))]
    (reduce random-value-to-board empty-board values)))
