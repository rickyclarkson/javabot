package javabot.javadoc;

import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;
import java.util.Set;
import java.util.TreeSet;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import javabot.dao.ApiDao;
import javabot.model.Persistent;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created Oct 29, 2008
 *
 * @author <a href="mailto:jlee@antwerkz.com">Justin Lee</a>
 */
@Entity
@Table(name = "apis")
@NamedQueries({
    @NamedQuery(name = ApiDao.FIND_BY_NAME, query = "select a from Api a where upper(a.name)=upper(:name)"),
    @NamedQuery(name = ApiDao.FIND_ALL, query = "select a from Api a order by a.name")
})
public class Api implements Persistent {
    private static final Logger log = LoggerFactory.getLogger(Api.class);
    private Long id;
    private String name;
    private String baseUrl;
    private String packages;
    private String zipLocations;
    private List<Clazz> classes = new ArrayList<Clazz>();
    private static final List<String> JDK_JARS = Arrays.asList("classes.jar", "rt.jar", "jce.jar", "jsse.jar");

    public Api() {
    }

    public Api(final String apiName, final String url, String pkgs, String zip) {
        name = apiName;
        baseUrl = url.endsWith("/") ? url : url + "/";
        packages = pkgs;
        zipLocations = zip;
        if ("JDK".equals(name)) {
            zipLocations = findJDKJars();
        }
    }

    @Id
    @GeneratedValue
    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    @Column(length = 1000)
    public String getBaseUrl() {
        return baseUrl;
    }

    public void setBaseUrl(final String baseUrl) {
        this.baseUrl = baseUrl;
    }

    @OneToMany(cascade = CascadeType.REMOVE, mappedBy = "api")
    public List<Clazz> getClasses() {
        return classes;
    }

    public void setClasses(final List<Clazz> classes) {
        this.classes = classes;
    }

    public String getPackages() {
        return packages;
    }

    public void setPackages(final String packages) {
        this.packages = packages;
    }

    public String getZipLocations() {
        return zipLocations;
    }

    public void setZipLocations(final String zipLocations) {
        this.zipLocations = zipLocations;
    }

    private String findJDKJars() {
        Set<String> jars = new TreeSet<String>();
        String paths = System.getProperty("sun.boot.class.path");
        if (paths == null) {
            paths = System.getProperty("java.class.path");
        }
        for (String path : paths.split(File.pathSeparator)) {
            File file = new File(path);
            if (JDK_JARS.contains(file.getName())) {
                try {
                    jars.add(file.getCanonicalFile().toURI().toURL().toString());
                } catch (MalformedURLException e) {
                    throw new RuntimeException(e.getMessage());
                } catch (IOException e) {
                    throw new RuntimeException(e.getMessage());
                }
            }
        }
        return StringUtils.join(jars, ",");
    }
}
