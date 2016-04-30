package org.univ7.webapp.quiz.entity;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Data
@ToString
@Entity
@Table(name = "Question")
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(exclude = "examples")
public class Question {

	@Id
	@GeneratedValue
	@Column(name = "QUESTION_ID")
	private Long id;

	@Column(name = "DESCRIPTION")
	private String description;

	@OneToMany(mappedBy = "question", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private List<Example> examples = new ArrayList<Example>();
}
