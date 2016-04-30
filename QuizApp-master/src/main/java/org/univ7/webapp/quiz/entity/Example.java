package org.univ7.webapp.quiz.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "Example")
@Data
@ToString
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(exclude = "question")
public class Example {

	@Id
	@GeneratedValue
	@Column(name = "EXAMPLE_ID")
	private Long id;

	@Column(name = "EXAMPLE_SENTENCE")
	private String exampleSentence;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "QUESTION_ID")
	private Question question;
}
