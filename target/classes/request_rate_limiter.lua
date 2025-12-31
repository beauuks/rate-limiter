local key = KEYS[1] --user id
local capacity = tonumber(ARGV[1])
local refill_rate = tonumber(ARGV[2]) -- tokens per second
local now = tonumber(ARGV[3]) -- current time stamp

-- Get the last time user was updated
local last_refill_time = tonumber(redis.call('hget', key, 'last_refill_time') or now)
-- Get the current tokens
local tokens = tonumber(redis.call('hget', key, 'tokens') or capacity)

--how many tokens to add based on time passed
local time_passed = math.max(0, now - last_refill_time)
local new_tokens = time_passed * refill_rate

-- Refill the bucket 
tokens = math.min(capacity, tokens + new_tokens)

-- Check if we have enough tokens for this request
if tokens >= 1 then
    tokens = tokens - 1

    redis.call('hset', key, 'last_refill_time', now)
    redis.call('hset', key, 'tokens', tokens)

    redis.call('expire', key, 60)
    return 1 
else
    return 0 
end