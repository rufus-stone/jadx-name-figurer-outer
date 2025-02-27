###### Class com.tfoc.hello.Baz (com.tfoc.hello.Baz)
.class public Lcom/tfoc/hello/Baz;
.super Ljava/lang/Object;
.source "Baz.java"


# instance fields
.field bar:Lcom/tfoc/hello/Bar;


# direct methods
.method public constructor <init>()V
    .registers 2

    .line 13
    invoke-direct {p0}, Ljava/lang/Object;-><init>()V

    .line 14
    new-instance v0, Lcom/tfoc/hello/Bar;

    invoke-direct {v0}, Lcom/tfoc/hello/Bar;-><init>()V

    iput-object v0, p0, Lcom/tfoc/hello/Baz;->bar:Lcom/tfoc/hello/Bar;

    .line 15
    return-void
.end method


# virtual methods
.method public doThing(Lorg/json/JSONObject;)V
    .registers 7
    .param p1, "json"    # Lorg/json/JSONObject;

    .line 19
    const-string v0, "device_name"

    const-string v1, "Baz"

    :try_start_4
    iget-object v2, p0, Lcom/tfoc/hello/Baz;->bar:Lcom/tfoc/hello/Bar;

    const-string v3, "secret"

    invoke-virtual {p1, v3}, Lorg/json/JSONObject;->getString(Ljava/lang/String;)Ljava/lang/String;

    move-result-object v3

    iput-object v3, v2, Lcom/tfoc/hello/Bar;->a:Ljava/lang/String;

    .line 20
    iget-object v2, p0, Lcom/tfoc/hello/Baz;->bar:Lcom/tfoc/hello/Bar;

    invoke-virtual {p1, v0}, Lorg/json/JSONObject;->getString(Ljava/lang/String;)Ljava/lang/String;

    move-result-object v3

    iput-object v3, v2, Lcom/tfoc/hello/Bar;->b:Ljava/lang/String;

    .line 21
    iget-object v2, p0, Lcom/tfoc/hello/Baz;->bar:Lcom/tfoc/hello/Bar;

    const-string v3, "age"

    invoke-virtual {p1, v3}, Lorg/json/JSONObject;->getInt(Ljava/lang/String;)I

    move-result v3

    iput v3, v2, Lcom/tfoc/hello/Bar;->c:I

    .line 22
    iget-object v2, p0, Lcom/tfoc/hello/Baz;->bar:Lcom/tfoc/hello/Bar;

    const-string v3, "epoch_nanos"

    invoke-virtual {p1, v3}, Lorg/json/JSONObject;->getLong(Ljava/lang/String;)J

    move-result-wide v3

    iput-wide v3, v2, Lcom/tfoc/hello/Bar;->d:J

    .line 23
    iget-object v2, p0, Lcom/tfoc/hello/Baz;->bar:Lcom/tfoc/hello/Bar;

    const-string v3, "pi"

    invoke-virtual {p1, v3}, Lorg/json/JSONObject;->getDouble(Ljava/lang/String;)D

    move-result-wide v3

    iput-wide v3, v2, Lcom/tfoc/hello/Bar;->e:D

    .line 24
    iget-object v2, p0, Lcom/tfoc/hello/Baz;->bar:Lcom/tfoc/hello/Bar;

    const-string v3, "evil"

    invoke-virtual {p1, v3}, Lorg/json/JSONObject;->getBoolean(Ljava/lang/String;)Z

    move-result v3

    iput-boolean v3, v2, Lcom/tfoc/hello/Bar;->f:Z

    .line 26
    invoke-virtual {p1, v0}, Lorg/json/JSONObject;->getString(Ljava/lang/String;)Ljava/lang/String;

    move-result-object v0

    .line 27
    .local v0, "local":Ljava/lang/String;
    invoke-static {v1, v0}, Landroid/util/Log;->d(Ljava/lang/String;Ljava/lang/String;)I
    :try_end_45
    .catch Lorg/json/JSONException; {:try_start_4 .. :try_end_45} :catch_47

    .line 33
    nop

    .line 34
    .end local v0    # "local":Ljava/lang/String;
    return-void

    .line 30
    :catch_47
    move-exception v0

    .line 31
    .local v0, "e":Lorg/json/JSONException;
    const-string v2, "Oopsie!"

    invoke-static {v1, v2}, Landroid/util/Log;->d(Ljava/lang/String;Ljava/lang/String;)I

    .line 32
    new-instance v1, Ljava/lang/RuntimeException;

    invoke-direct {v1, v0}, Ljava/lang/RuntimeException;-><init>(Ljava/lang/Throwable;)V

    throw v1
.end method
