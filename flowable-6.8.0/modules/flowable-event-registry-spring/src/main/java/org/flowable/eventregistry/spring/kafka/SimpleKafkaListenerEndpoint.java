/* Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.flowable.eventregistry.spring.kafka;

import java.util.Collection;
import java.util.Collections;
import java.util.Properties;
import java.util.regex.Pattern;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.kafka.config.KafkaListenerEndpoint;
import org.springframework.kafka.listener.GenericMessageListener;
import org.springframework.kafka.listener.MessageListenerContainer;
import org.springframework.kafka.support.TopicPartitionOffset;
import org.springframework.kafka.support.converter.MessageConverter;
import org.springframework.util.Assert;
import org.springframework.util.ObjectUtils;

/**
 * @author Filip Hrisafov
 */
public class SimpleKafkaListenerEndpoint<K, V> implements KafkaListenerEndpoint, InitializingBean {

    protected String id;
    protected String groupId;
    protected Collection<String> topics;
    protected Pattern topicPattern;
    protected Collection<TopicPartitionOffset> topicPartitions;
    protected String clientIdPrefix;
    protected Integer concurrency;
    protected Properties consumerProperties;
    protected boolean splitIterables = true;

    protected GenericMessageListener<ConsumerRecord<K, V>> messageListener;

    public void setMessageListener(GenericMessageListener<ConsumerRecord<K, V>> messageListener) {
        this.messageListener = messageListener;
    }

    public GenericMessageListener<ConsumerRecord<K, V>> getMessageListener() {
        return messageListener;
    }

    @Override
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    @Override
    public String getGroup() {
        return null;
    }

    @Override
    public Collection<String> getTopics() {
        return topics != null ? topics : Collections.emptyList();
    }

    public void setTopics(Collection<String> topics) {
        this.topics = topics;
    }

    @Override
    public TopicPartitionOffset[] getTopicPartitionsToAssign() {
        return topicPartitions == null ? null : topicPartitions.toArray(new TopicPartitionOffset[0]);
    }

    public void setTopicPartitions(Collection<TopicPartitionOffset> topicPartitions) {
        this.topicPartitions = topicPartitions;
    }

    @Override
    public Pattern getTopicPattern() {
        return topicPattern;
    }

    public void setTopicPattern(Pattern topicPattern) {
        this.topicPattern = topicPattern;
    }

    @Override
    public String getClientIdPrefix() {
        return clientIdPrefix;
    }

    public void setClientIdPrefix(String clientIdPrefix) {
        this.clientIdPrefix = clientIdPrefix;
    }

    @Override
    public Integer getConcurrency() {
        return concurrency;
    }

    public void setConcurrency(Integer concurrency) {
        this.concurrency = concurrency;
    }

    @Override
    public Boolean getAutoStartup() {
        return null;
    }

    @Override
    public Properties getConsumerProperties() {
        return consumerProperties;
    }

    public void setConsumerProperties(Properties consumerProperties) {
        this.consumerProperties = consumerProperties;
    }

    @Override
    public void setupListenerContainer(MessageListenerContainer listenerContainer, MessageConverter messageConverter) {
        GenericMessageListener<ConsumerRecord<K, V>> messageListener = getMessageListener();
        Assert.state(messageListener != null, () -> "Endpoint [" + this + "] must provide a non null message listener");
        listenerContainer.setupMessageListener(messageListener);
    }

    @Override
    public boolean isSplitIterables() {
        return splitIterables;
    }

    public void setSplitIterables(boolean splitIterables) {
        this.splitIterables = splitIterables;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        boolean topicsEmpty = getTopics().isEmpty();
        boolean topicPartitionsEmpty = ObjectUtils.isEmpty(getTopicPartitionsToAssign());
        if (!topicsEmpty && !topicPartitionsEmpty) {
            throw new IllegalStateException("Topics or topicPartitions must be provided but not both for " + this);
        }
        if (this.topicPattern != null && (!topicsEmpty || !topicPartitionsEmpty)) {
            throw new IllegalStateException("Only one of topics, topicPartitions or topicPattern must are allowed for "
                    + this);
        }
        if (this.topicPattern == null && topicsEmpty && topicPartitionsEmpty) {
            throw new IllegalStateException("At least one of topics, topicPartitions or topicPattern must be provided "
                    + "for " + this);
        }
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "[" + this.id
            + "] topics=" + this.topics
            + "' | topicPattern='" + this.topicPattern + "'"
            + "' | topicPartitions='" + this.topicPartitions + "'"
            + " | messageListener='" + messageListener + "'";
    }
}
