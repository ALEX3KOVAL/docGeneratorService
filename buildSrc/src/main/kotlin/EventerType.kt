enum class EventerType(val repo: String) {
    KAFKA("kafkaEventer"),
    RABBIT_MQ("rabbitEventer"),
    REDIS("redisEventer");
}
