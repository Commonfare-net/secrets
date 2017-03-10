(ns fxc.intcomp
  (:gen-class)
  (:import [me.lemire.integercompression IntWrapper
            IntCompressor BinaryPacking FastPFOR
            Composition VariableByte FastPFOR128 IntegerCODEC]
           [me.lemire.integercompression.differential
            IntegratedIntegerCODEC IntegratedComposition
            IntegratedBinaryPacking IntegratedVariableByte]))


(defn codec.binpack []
  (IntegratedComposition. (IntegratedBinaryPacking.) (IntegratedVariableByte.)))

(defn codec.pfor128 []
  (Composition. (FastPFOR128.) (VariableByte.)))

(defn codec.pfor []
  (Composition. (FastPFOR.) (VariableByte.)))

(defn compress
  ([intarr] 
   (seq (.compress (IntCompressor.) (int-array intarr))))

  ;; non-default codecs used only for debugging
  ([cx intarr]
   (let [len (count intarr)
         res (int-array (repeat len 0))
         inpos  0
         outpos 0 ]
     (.compress cx (int-array intarr) (IntWrapper. inpos) len res (IntWrapper. outpos))
  {:codec (str cx)
   :inpos inpos
   :outpos outpos
   :data res})))


(defn decompress
  ([intarr] (seq (.uncompress (IntCompressor.) (int-array intarr))))

  ;; non-default codecs used only for debugging
  ([cx intarr]
   (let [len (count intarr)
         res (int-array (repeat len 0))
         inpos  0
         outpos 0 ]
     (.uncompress cx (int-array intarr) (IntWrapper. inpos) len res (IntWrapper. outpos))
     {:codec (str cx)
      :inpos inpos
      :outpos outpos
      :data res})))

