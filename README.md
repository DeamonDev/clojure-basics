# clojure-basics

My Clojure learning project. I'm an experienced developer, but new to Clojure, and
I'm here for two things:

1. **Understand the philosophy of the language**, not just its syntax: why Clojure
   is built the way it is, and how that changes the way I write programs.
2. **Master Paredit**, editing code as a tree of forms instead of a string of
   characters, until it feels faster than typing parens by hand.

The vehicle is a set of fill-in-the-blank challenges: each one is a comment
describing the task plus a stub to replace with real code, checked by tests.

## The philosophy I'm trying to absorb

| Idea | What it means in practice | Where I practice it |
|---|---|---|
| **Code is data** | Programs are lists, vectors and maps. Parens *are* the structure. | everywhere, Paredit, macros (19) |
| **Simple over easy** | Small functions over plain data; no classes holding state and behaviour together. | 01-07 |
| **Data first** | Maps and seqs instead of custom types; a few generic functions over them. | 03, 06, 07, 10, 11 |
| **Immutability by default** | "Changing" data returns a new value; the old one stays intact. | 10, 17 |
| **Sequences & laziness** | One abstraction over every collection; infinite seqs are fine. | 04, 05, 08, 09 |
| **Functions are values** | Pass, return and compose them; build behaviour from small pieces. | 04, 11, 12, 13 |
| **State is explicit and rare** | Pure functions do the work; atoms mark the few places where things change. | 13, 18 |
| **Polymorphism à la carte** | Multimethods and protocols, decoupled from inheritance; extend types I don't own. | 14, 15 |
| **Embrace the host** | Java is right there; interop instead of wrappers. | 15, 16 |
| **REPL-driven development** | Grow the program live, one form at a time, instead of edit-compile-run. | the whole workflow |

## Paredit goals

Structural editing is on, always. Rules I hold myself to:

- **Never type a closing paren.** If parens are unbalanced, I stop and figure out how
  I got there.
- **Move forms, not characters.** Before reaching for backspace, ask: is this a
  slurp, barf, splice, raise or wrap?
- **Bind the commands to keys** and use them until they're reflexes.

| Command | Before → after (`\|` = cursor) |
|---|---|
| Slurp forward | `(foo \|a) b` → `(foo \|a b)` |
| Barf forward | `(foo \|a b)` → `(foo \|a) b` |
| Splice | `(foo (\|a b))` → `(foo \|a b)` |
| Raise | `(foo (bar \|x))` → `(foo \|x)` |
| Wrap with `()` | `\|x` → `(\|x)` |

## Layout

```
src/first_steps/core.clj              small worked example (greet, fizzbuzz)
test/first_steps/core_test.clj        its tests
src/first_steps/challenges.clj        my work: replace each (todo)
test/first_steps/challenges_test.clj  tests for the challenges
```

Namespace `first-steps.core` lives in `first_steps/core.clj`: dashes become
underscores in file paths (a JVM limitation).

## Workflow

1. Start a REPL with the `dev` alias (`clj -A:dev`, or select `dev` in the IDE;
   never `test`, which runs the test runner instead of a REPL).
2. Open `challenges.clj`, **Load file in REPL**, switch the REPL to its namespace.
3. Write the function. Explore pieces in a `(comment ...)` block, sending one form at a time.
4. **Send top form** to redefine the function, then run its test.
5. Make it pass, then make it idiomatic (e.g. rewrite nested calls with `->>`).

Running from the command line:

```sh
clojure -M:test                                                  # everything
clojure -M:test -n first-steps.challenges-test                   # all challenges
clojure -M:test -v first-steps.challenges-test/c01-square-test   # one challenge
clojure -M -m first-steps.core Ada                               # run -main
```

## Lessons learned so far

- `(f a b)`: the first thing in a list is **always** what gets called.
  `ClassCastException ... cannot be cast to clojure.lang.IFn` means I put a
  non-function there (e.g. infix `#(% * %)`).
- `cond` pairs are flat: `(cond test expr test expr :else expr)`, no extra parens.
- The REPL lives in a namespace. "Unable to resolve symbol" right after loading a
  file usually means the REPL is still in `user`.
- `-A` vs `-M`: aliases with `:main-opts` are for `-M`; REPL aliases shouldn't have any.

## Resources

- `(doc f)` and `(source f)` in the REPL
- https://clojuredocs.org: community examples for every core function
- Rich Hickey's talks: *Simple Made Easy*, *The Value of Values*, *Are We There Yet?*
