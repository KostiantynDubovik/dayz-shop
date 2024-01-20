package ua.com.oak.shop.jpa.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;
import org.hibernate.Hibernate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Locale;
import java.util.Objects;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Entity
@Table(name = "languages")
@JsonIgnoreProperties(value = {"hibernateLazyInitializer","handler"})
public class Language {
	@Id
	@Column(name = "LANGUAGE_ID", nullable = false)
	private Long id;

	@Column(name = "LANGUAGE")
	private String language;

	@Column(name = "COUNTRY")
	private String country;

	@Column(name = "LOCALE")
	private Locale locale;

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
		Language language = (Language) o;
		return id != null && Objects.equals(id, language.id);
	}

	@Override
	public int hashCode() {
		return getClass().hashCode();
	}
}
