(ns ikcrud.core
  (:require [clojure.core.match :refer [match]]
            (clojure [string :as clj-str]))
  (:import [jline.console ConsoleReader]))


(def main-options ["[S] - Show all class mates" 
                   "[A] - Add new class mate" 
                   "[U] - Edit a class mate"
                   "[D] - Delete class mate" 
                   "[F] - Find class mate" 
                   "[E] - Exit"])

(def delete-options ["[F] - First name"
                     "[L] - Last name"])

(def find-options ["[F] - First name"
                   "[L] - Last name"
                   "[R] - Role"
                   "[V] - Full name"])


(defrecord ClassMate [first last role])

(def class-mates-db (atom [(ClassMate. "Augustine" "Ogwo" "ClassBlogger")
                          (ClassMate. "Chibuike" "Ugwoke" "Class Rep")
                          (ClassMate. "Chibuzo" "Okolo" "Class Programmer")
                          (ClassMate. "Chima" "Okere" "Class Clown")
                          (ClassMate. "Chimere" "Ugwebgbu" "Class Salesman")]))

(defn print-mate [the-mate]
  (if the-mate
    (println ">>>" (:first the-mate) " " (:last the-mate) 
             ", role: " (:role the-mate))
    (println ">>>No class mate to print")))


;;; For showing class mates
(defn show-all-class-mates []
  (println "\nShowing all class mates ...")
  
  (doseq [class-mate @class-mates-db]
    (print-mate class-mate))
  (println "\nAll done!\n"))


;;; For adding class mates
(declare get-name-from-user class-mate-exists?)

(defn add-class-mate []
  (let [first-name (get-name-from-user "First name")
        last-name (get-name-from-user "Last-name")
        role (get-name-from-user "Role")]
    (if (class-mate-exists? first-name last-name)
      (do 
        (println "\nSorry, That class mate already exists!\n")
        true)
      (do 
        (reset! class-mates-db (conj @class-mates-db 
                                     {:first first-name :last last-name
                                      :role role}))
        (println "\nNew class mate successfully added!")
        (show-all-class-mates)))))


;;; This is getting the first name and the last name
;;; and the role for when the user wants to add a class mate
(defn get-name-from-user [field-title]
  (let [msg-prompt (str "Please enter the " field-title "\n")
        cr (ConsoleReader.)
        the-input (.readLine cr msg-prompt)]
    (cond 
      (empty? the-input) (recur field-title)
      (.equalsIgnoreCase the-input "Z") :main-menu 
      :else the-input)))

(defn class-mate-exists? [first-name last-name]
  (let [found (for [class-mate @class-mates-db
                    :when (or (.equalsIgnoreCase (:first class-mate) 
                                first-name)
                              (.equalsIgnoreCase (:last class-mate) 
                                last-name))]
                class-mate)]
    (if (> (count found) 0)
      true false)))

(declare get-name-for-edit get-new-edit-names)

;;; For editing class mate details
(defn edit-class-mate []
  (println "Update menu - by [F] - First name or [L] - Last Name")
  (let [cr (ConsoleReader.)
        user-input (.readCharacter cr)]
    (cond 
     \F (do (println "Type first name to edit")
            (get-name-for-edit :first nil)
            true) 
     \L (do (println "Type last name to edit") 
            (get-name-for-edit nil :last) 
            true)
     :else false)))

(defn get-name-for-edit [by-first by-last]
  (let [cr (ConsoleReader.)
        user-input (.readLine cr)]
    (cond 
     (= by-first :first) (if (class-mate-exists? user-input "")
                           (do 
                             (println "Got the class mate with that first name.\n") 
                             (get-new-edit-names :first nil user-input)
                             true))
     (= by-last :last) (if (class-mate-exists? "" user-input)
                         (do 
                           (println "Got the class mate with that last name.\n") 
                           (get-new-edit-names nil :last user-input)
                           true)))))

(defn get-new-edit-names [first-name-edit last-name-edit 
                          the-name] 
  (let [first-name (get-name-from-user "First name")
        last-name (get-name-from-user "Last name")
        role (get-name-from-user "Role")]
    (do 
      (if first-name-edit
        (reset! class-mates-db 
                (filter #(not (or (= (:first %) the-name)))
                        @class-mates-db))
        (reset! class-mates-db 
                (filter #(not (or (= (:last %) the-name)))
                        @class-mates-db)))
      (reset! class-mates-db (conj @class-mates-db 
                                   {:first first-name :last last-name
                                    :role role}))
      (show-all-class-mates)
      true)))


(declare get-delete-name do-actual-delete)

;;; For deleting class mates
(defn delete-class-mate []
  (println "Delete menu - Choose an option for deletion!\n\n")
  (doseq [option delete-options]
    (println option))
  (println "\n")
  (flush)
  
  (let [cr (ConsoleReader.)
        the-char (char (.readCharacter cr))]
    (case (Character/toUpperCase the-char)
      \F (do (println "You can type the first name.")
           (get-delete-name :first nil)
           true)
      \L (do (println "You can type the last name.")
           (get-delete-name nil :last)
           true)
      (recur))))

(defn get-delete-name [delete-first delete-last]
 (let [cr (ConsoleReader.)
       the-str (.readLine cr)
       mate-exists (match [delete-first delete-last]
                          [:first _] (class-mate-exists? the-str "")
                          [_ :last] (class-mate-exists? "" the-str))]
   (if mate-exists
     (do 
       (do-actual-delete the-str)
       (println "Class mate deleted!")
       (show-all-class-mates)
       true)
     (do 
       (println "Class mate with that name does NOT exist.")
       true))))

(defn do-actual-delete [user-input]
  (reset! class-mates-db 
          (filter #(not (or (= (:first %) user-input)
                             (= (:last %) user-input)))
                  @class-mates-db)))


;;; For Finding class mates
(declare act-on-find-option do-actual-find 
         find-by do-find reverse-name)

(defn find-class-mate-options []
  (println "Find menu - Choose an option for finding\n")
  (doseq [option find-options]
    (println option))
  (println "\n")
  (flush)
  
  (let [cr (ConsoleReader.)
        the-char (char (.readCharacter cr))]
    (case (Character/toUpperCase the-char)
      \F (do (println "You can type the first name!")
           (do-find :first nil nil nil)
           true)
      \L (do (println "You can type the last name!")
           (do-find nil :last nil nil)
           true)
      \R (do (println "You can type the role")
           (do-find nil nil :role nil)
           true)
      \V  (do (println "You can type the full name. First name first.")
           (do-find nil nil nil :full)
           true)
      (recur))))

(defn do-find [find-first find-last find-role find-full]
  (let [cr (ConsoleReader.)
        the-str (.readLine cr)
        result-set (do-actual-find find-first find-last 
                                   find-role find-full the-str)]
    (println (str "Search word: " the-str 
                  ", Num of results: " (count result-set)))
    (dorun (map print-mate result-set))))

(defn do-actual-find [find-first find-last 
                      find-role find-full user-input] 
  (match [find-first find-last find-role find-full] 
         [:first _ _ _] (find-by :first user-input) 
         [_ :last _ _] (find-by :last user-input)
         [_ _ :role _] (find-by :role user-input)
         [_ _ _ :full] (for [class-mate @class-mates-db
                             :let [full-name (str (:first class-mate) " " 
                                                  (:last class-mate))]
                             :when (or (= full-name 
                                         user-input)
                                       (= (reverse-name full-name )
                                         user-input))]
                         class-mate)
         :else nil))

(defn find-by [search-func user-input]
  (for [class-mate @class-mates-db
        :when (.equalsIgnoreCase 
                (search-func class-mate) user-input)]
    class-mate))

(defn reverse-name [the-name]
  (clj-str/join " " 
                (rseq (clj-str/split the-name #" "))))



(defn main-option-selected [^Character the-char]
  (case (Character/toUpperCase the-char)
    \S (do (show-all-class-mates) true)  
    \A (do (add-class-mate) true) 
    \U (do (edit-class-mate) true) 
    \D (do (delete-class-mate) true)
    \F (do (find-class-mate-options) true)
    \E (System/exit 0)
    \space false
    
    false))


;;; Where all the magic begins
(defn -main []
  (println "Main Menu - Choose a valid option.\n\n")
  (doseq [option main-options]
    (println option))
  (println "\n")
  (flush)
  
  (let [cr (ConsoleReader.)
        the-char (char (.readCharacter cr))]
    (println (format "You typed: '%c'!" the-char))
    
    (if (not (main-option-selected the-char))
      (println "Bad input brother!\n"))
    (recur)))
