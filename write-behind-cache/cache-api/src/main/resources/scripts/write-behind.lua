local ok = redis.call("SET", KEYS[1] .. ":idem", "1", "NX", "EX", 86400)
if not ok then
    return "DUPLICATE"
end

local streamId = redis.call("XADD", KEYS[2], "*",
    "eventId", KEYS[1],
    "event", ARGV[1]
)

return streamId