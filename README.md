
## JADX Name Figurer-Outer plugin

Simple plugin to rename fields based on the strings passed to methods like JSONObject's getString() and friends.

### Usage

Turns something like this:

```java

this.a = json.getString("importantString");
this.b = json.getInt("someNumber");

```

...into something like this:

```java

this.importantString = json.getString("importantString");
this.someNumber = json.getInt("someNumber");

```

### Installation

Install using location id: `github:rufus-stone:jadx-name-figurer-outer`

In jadx-cli:
```bash
  jadx plugins --install "github:rufus-stone:jadx-name-figurer-outer"
```
